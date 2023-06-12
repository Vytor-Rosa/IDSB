package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.MinuteDTO;
import net.weg.gedesti.model.entity.*;
import net.weg.gedesti.model.service.ExpensesService;
import net.weg.gedesti.model.service.MinuteService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/minutes")
public class MinuteController {

    private MinuteService minuteService;
    private ExpensesService expensesService;

    @GetMapping
    public ResponseEntity<List<Minute>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(minuteService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Minute>> findAll(@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.status(HttpStatus.FOUND).body(minuteService.findAll(pageable));
    }

//    @PostMapping
//    public ResponseEntity<Object> save(@RequestParam(value = "minute") @Valid String minuteJson ) {
//        MinuteUtil util = new MinuteUtil();
//        Minute minute = util.convertJsonToModel(minuteJson);
//        return ResponseEntity.status(HttpStatus.CREATED).body(minuteService.save(minute));
//    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid MinuteDTO minuteDTO) {
        Minute minute = new Minute();
        BeanUtils.copyProperties(minuteDTO, minute);
        return ResponseEntity.status(HttpStatus.CREATED).body(minuteService.save(minute));
    }

    @GetMapping("/{minuteCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "minuteCode") Integer minuteCode) {
        Optional<Minute> minuteOptional = minuteService.findById(minuteCode);
        if (minuteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No minute with code:" + minuteCode);
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(minuteOptional);
    }

    @GetMapping("/type/{minuteType}")
    public ResponseEntity<Object> findByPublished(@PathVariable(value = "minuteType") String minuteType) {
        List<Minute> minutes = minuteService.findByMinuteType(minuteType);
        if (minutes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No minutes published");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(minutes);
    }

    @GetMapping("/agenda/{agendaCode}")
    public ResponseEntity<Object> findByAgenda(@PathVariable(value = "agendaCode") Agenda agendaCode) {
        List<Minute> minutes = minuteService.findByAgenda(agendaCode);
        if (minutes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No minutes published");
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(minutes);
    }

    @DeleteMapping("/{minuteCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "minuteCode") Integer minuteCode) {
        Optional<Minute> minuteOptional = minuteService.findById(minuteCode);
        if (minuteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No minute with code: " + minuteCode);
        }
        minuteService.deleteById(minuteCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Minute " + minuteCode + " successfully deleted!");
    }

    @GetMapping("/pdf/{minuteCode}")
    public void savePdf(@PathVariable(value = "minuteCode") Integer minuteCode, HttpServletResponse response) {
        Optional<Minute> minuteOptional = minuteService.findById(minuteCode);
        Minute minute = minuteOptional.get();

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float pageHeight = page.getMediaBox().getHeight();
            float margin = 100;
            float fontTitle = 12;
            float fontInformations = 10;
            float currentHeight = pageHeight - margin * 2; // Subtrai as margens superiores

            String path = new File(".").getCanonicalPath();
            PDImageXObject weg = PDImageXObject.createFromFile(path + "\\src\\main\\java\\net\\weg\\gedesti\\controller\\filePdf\\img.png", document);
            contentStream.drawImage(weg, 500, 730, 55, 40);


            contentStream.beginText();
            contentStream.newLineAtOffset(60, 760);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
            Color color = Color.decode("#00579D");
            contentStream.setNonStrokingColor(color);

            contentStream.newLineAtOffset(0, -20);

            List<Commission> commissionList = minute.getAgenda().getCommission();
            String comission = "";

            for (int i = 0; i < commissionList.size(); i++) {
                if (i + 1 == commissionList.size()) {
                    comission += commissionList.get(i).getCommissionName().split("–")[1];
                } else {
                    comission += commissionList.get(i).getCommissionName().split("–")[1] + " , ";
                }
            }

            contentStream.showText("ATA REUNIÃO" + comission.toUpperCase());
            contentStream.newLineAtOffset(0, -40);


            Color color2 = Color.decode("#00000");
            contentStream.setNonStrokingColor(color2);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);

            contentStream.newLineAtOffset(430, 0);
            contentStream.showText("ATA Nº: " + minute.getMinuteCode() + "/" + minute.getMinuteStartDate().split("/")[2]);
            contentStream.newLineAtOffset(0, -12);
            contentStream.showText("Data: " + minute.getMinuteStartDate());
            contentStream.newLineAtOffset(0, -12);
            contentStream.showText("Início: ");
            contentStream.newLineAtOffset(45, 0);
            contentStream.showText(minute.getAgenda().getInitialDate().split("T")[1]);
            contentStream.newLineAtOffset(-45, -12);
            contentStream.showText("Término: ");
            contentStream.newLineAtOffset(45, 0);
            contentStream.showText(minute.getAgenda().getFinalDate().split("T")[1]);
            contentStream.newLineAtOffset(0, -12);

            contentStream.newLineAtOffset(-475, 0);

            List<Proposal> proposalsList = minute.getAgenda().getProposals();

            Document doc = new Document("");
            StringBuilder lineBuilder = new StringBuilder();
            float currentWidth = 0;
            float maxWidth = 500;

            for (int i = 0; i < proposalsList.size(); i++) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
                contentStream.setNonStrokingColor(color);
                contentStream.showText(proposalsList.get(i).getProposalName() + " – " + proposalsList.get(i).getProposalCode());
                contentStream.newLineAtOffset(0, -20);

                contentStream.setNonStrokingColor(color2);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
                contentStream.showText("Objetivo: ");
                contentStream.setFont(PDType1Font.HELVETICA, 12);

                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                Document objective = Jsoup.parse(proposalsList.get(i).getDemand().getDemandObjective());
                showText(objective, contentStream);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Escopo do Projeto: ");
                contentStream.newLineAtOffset(0, -20);

                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                Document escope = Jsoup.parse(proposalsList.get(i).getDescriptiveProposal());
                showText(escope, contentStream);

                contentStream.newLineAtOffset(0, -20);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
                contentStream.showText("Resultados Esperados (Qualitativos): ");
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                Document benefitsQualitative = Jsoup.parse(proposalsList.get(i).getDemand().getQualitativeBenefit().getQualitativeBenefitDescription());
                showText(benefitsQualitative, contentStream);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Benefícios potencias: ");
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                Document benefitsPotential = Jsoup.parse(proposalsList.get(i).getDemand().getPotentialBenefit().getPotentialBenefitDescription());
                showText(benefitsPotential, contentStream);
                contentStream.newLineAtOffset(0, -20);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
                contentStream.showText("Periodo de execução: ");
                contentStream.newLineAtOffset(130, 0);
                contentStream.setFont(PDType1Font.HELVETICA, fontTitle);
                contentStream.showText(proposalsList.get(i).getInitialRunPeriod().getDay() + "/" + proposalsList.get(i).getInitialRunPeriod().getMonth() + "/" + proposalsList.get(i).getInitialRunPeriod().getYear() + " à " + proposalsList.get(i).getFinalExecutionPeriod().getDay() + "/" + proposalsList.get(i).getFinalExecutionPeriod().getMonth()  + "/" + proposalsList.get(i).getFinalExecutionPeriod().getYear());


                contentStream.newLineAtOffset(-130, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);

                contentStream.showText("Responsável pelo negócio: ");
                contentStream.setFont(PDType1Font.HELVETICA, fontTitle);
                contentStream.newLineAtOffset(160, 0);
                contentStream.showText(proposalsList.get(i).getWorkers().get(0).getWorkerName());
                contentStream.newLineAtOffset(-160, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
                contentStream.showText("Parecer da comissão: " + proposalsList.get(i).getProposalStatus());


                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.showText("Custos de Execução: ");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                List<Expenses> expensesList = expensesService.findAllByProposalProposalCode(proposalsList.get(i).getProposalCode());

                for (Expenses expenses : expensesList) {
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText(expenses.getExpensesType());
                    contentStream.newLineAtOffset(0, -20);

                    List<Expense> expenseList = expenses.getExpense();
                    List<ExpensesCostCenters> expensesCostCentersList = expenses.getExpensesCostCenters();

                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    contentStream.showText("DESPESAS");
                    for (Expense expense : expenseList) {
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText("Perfil da despesa: " );
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(expense.getExpenseProfile());
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.showText("Quantidade de horas: " );
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(expense.getAmountOfHours() + "h");
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.showText("Valor hora: R$" );
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(String.valueOf(expense.getHourValue()));
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.showText("Valor total: R$" );
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(String.valueOf(expense.getTotalValue()));
                    }

                    for (ExpensesCostCenters expensesCostCenters : expensesCostCentersList) {
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                        contentStream.showText("Centro de custo: ");
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.showText(expensesCostCenters.getCostCenter().getCostCenter() + " - " + expensesCostCenters.getPercent()+ "%");
                    }
                }

            }

            contentStream.endText();
            contentStream.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            document.close();

            byte[] byteArray = byteArrayOutputStream.toByteArray();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=file.pdf");
            response.setHeader("Content-Length", String.valueOf(byteArray.length));
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.getOutputStream().write(byteArray);
            response.getOutputStream().flush();
            response.getOutputStream().close();

            ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showText(Document document, PDPageContentStream contentStream) throws IOException {
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\n");

        for(int j = 0; j < document.text().length(); j++) {
            if(j+1 == document.text().length()) {
                contentStream.showText(String.valueOf(document.text().charAt(j)));
                break;
            }
            if(j == 0){
                contentStream.showText(String.valueOf(document.text().charAt(j)));
            } else {
                String n = document.text().charAt(j) + "" + document.text().charAt(j + 1);
                String n2 = document.text().charAt(j - 1) + "" + document.text().charAt(j);

                if(!n2.equals("\\n")){
                    if (n.equals("\\n")) {
                        contentStream.newLineAtOffset(0, -20);
                    } else {
                        contentStream.showText(String.valueOf(document.text().charAt(j)));
                    }
                }
            }
        }
    }


}
