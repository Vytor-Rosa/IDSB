package net.weg.gedesti.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.AgendaDTO;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.dto.ProposalDTO;
import net.weg.gedesti.model.entity.*;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.model.service.ExpenseService;
import net.weg.gedesti.model.service.ExpensesService;
import net.weg.gedesti.model.service.ProposalService;
import net.weg.gedesti.repository.ExpenseRepository;
import net.weg.gedesti.repository.ProposalRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.jsoup.Jsoup;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/proposal")
public class ProposalController {
    private ProposalService proposalService;
    private ProposalRepository proposalRepository;
    private ExpensesService expensesService;
    private DemandService demandService;

    @GetMapping
    public ResponseEntity<List<Proposal>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(proposalService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Proposal>> findAll(@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());

        List<Proposal> proposals = proposalService.findAll();
        for (Proposal proposal : proposals) {
            if (proposal.getScore() == null) {
                proposal.setScore(score(proposal.getDemand()));
                proposalRepository.saveAndFlush(proposal);
            }
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(proposalService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ProposalDTO proposalDTO) {
        Proposal proposal = new Proposal();
        BeanUtils.copyProperties(proposalDTO, proposal);
        proposal.setScore(proposal.getDemand().getScore());
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalService.save(proposal));
    }

    @GetMapping("/{proposalCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "proposalCode") Integer proposalCode) throws IOException {
        Optional<Proposal> proposalOptional = proposalService.findById(proposalCode);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No proposal with code: " + proposalCode);
        }

        //savePdf(proposalOptional.get());
        return ResponseEntity.status(HttpStatus.FOUND).body(proposalOptional);
    }

    @DeleteMapping("/{proposalCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "proposalCode") Integer proposalCode) {
        Optional<Proposal> proposalOptional = proposalService.findById(proposalCode);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No proposal with code: " + proposalCode);
        }
        proposalService.deleteById(proposalCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Proposal " + proposalCode + " successfully deleted!");
    }


    @Modifying
    @Transactional
    @PutMapping("/status/{proposalCode}")
    public ResponseEntity<Object> addOpinion(@PathVariable(value = "proposalCode") Integer proposalCode, @RequestBody ProposalDTO proposalDTO) {
        if (!proposalService.existsById(proposalCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Proposal proposal = proposalRepository.findById(proposalCode).get();
        proposal.setProposalStatus(proposalDTO.getProposalStatus());
        proposal.setCommissionOpinion(proposalDTO.getCommissionOpinion());
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalRepository.saveAndFlush(proposal));
    }

    @Modifying
    @Transactional
    @PutMapping("/published/{proposalCode}")
    public ResponseEntity<Object> updatePublish(@PathVariable(value = "proposalCode") Integer proposalCode, @RequestBody ProposalDTO proposalDTO) {
        if (!proposalService.existsById(proposalCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }
        Proposal proposal = proposalRepository.findById(proposalCode).get();
        proposal.setPublished(proposalDTO.getPublished());
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalRepository.saveAndFlush(proposal));
    }

    @GetMapping("/demand/{demandCode}")
    public ResponseEntity<Object> findByDemandCode(@PathVariable(value = "demandCode") Integer demandCode) {
        Optional<Proposal> proposalOptional = proposalService.findByDemandDemandCode(demandCode);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FOUND).body(demandCode);
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(proposalOptional);
        }
    }

    @Modifying
    @Transactional
    @PutMapping("/{proposalCode}")
    public ResponseEntity<Object> update(@PathVariable(value = "proposalCode") Integer proposalCode, @RequestBody ProposalDTO proposalDTO) {
        if (!proposalService.existsById(proposalCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }
        Proposal proposal = proposalRepository.findById(proposalCode).get();
        BeanUtils.copyProperties(proposalDTO, proposal);
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalRepository.saveAndFlush(proposal));
    }

    public Double score(Demand demand) {
        Integer demandSize = 0;

        List<Demand> demandList = demandService.findAllByDemandCode(demand.getDemandCode());
        Demand demandDate = new Demand();

        for (Demand demand1 : demandList) {
            if (demand1.getDemandVersion() == 1) {
                demandDate = demand1;
            }
        }

        LocalDate actualDate = LocalDate.now();
        String createDate = demandDate.getDemandDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        long days = ChronoUnit.DAYS.between(LocalDate.parse(createDate, formatter), actualDate);

        if (demand.getClassification().getClassificationSize().equals("Muito Pequeno")) {
            demandSize = 1;
        } else if (demand.getClassification().getClassificationSize().equals("Pequeno")) {
            demandSize = 41;
        } else if (demand.getClassification().getClassificationSize().equals("Médio")) {
            demandSize = 301;
        } else if (demand.getClassification().getClassificationSize().equals("Grande")) {
            demandSize = 1001;
        } else if (demand.getClassification().getClassificationSize().equals("Muito Grande")) {
            demandSize = 3001;
        }
        Double score = ((2 * demand.getRealBenefit().getRealMonthlyValue()) + demand.getPotentialBenefit().getPotentialMonthlyValue() + days) / demandSize;
        return score;
    }


    @GetMapping("/pdf/{proposalCode}")
    public ResponseEntity<Object> savePdf(@PathVariable(value = "proposalCode") Integer proposalCode, HttpServletResponse response) throws IOException, DocumentException {
        try {
            Optional<Proposal> optionalProposal = proposalService.findById(proposalCode);
            Proposal proposal = optionalProposal.get();
            Demand demand = proposal.getDemand();


            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, new FileOutputStream("C:\\Users\\" + System.getProperty("user.name") + "\\Downloads\\iTextHelloWorld.pdf"));
            document.open();

            String path = new File(".").getCanonicalPath();
            com.itextpdf.text.Image logo = Image.getInstance(path + "\\src\\main\\java\\net\\weg\\gedesti\\controller\\filePdf\\img.png");
            logo.setAlignment(Element.ALIGN_RIGHT);
            logo.scaleToFit(50, 50);
            document.add(logo);

            String hexColor = "#00579D";
            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
            BaseColor baseColor = new BaseColor(red, green, blue);

            Paragraph title = new Paragraph(new Phrase(20F, demand.getDemandTitle() + " - "+ demand.getDemandCode(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, baseColor)));
            document.add(title);
            Paragraph quebra = new Paragraph();
            quebra.add(" ");
            document.add(quebra);

            com.itextpdf.text.Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            com.itextpdf.text.Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // Data
            Paragraph dateAndHour = new Paragraph();
            Chunk boldChunk = new Chunk("Data: ");
            boldChunk.setFont(fontBold);
            Chunk normalChunk = new Chunk(demand.getDemandDate() + " - " + demand.getDemandHour() + "h");
            normalChunk.setFont(fontNormal);
            dateAndHour.add(boldChunk);
            dateAndHour.add(normalChunk);
            document.add(dateAndHour);

            // Solicitante
            Paragraph requester = new Paragraph();
            boldChunk = new Chunk("Solicitante: ");
            boldChunk.setFont(fontBold);
            normalChunk = new Chunk(demand.getRequesterRegistration().getWorkerName());
            normalChunk.setFont(fontNormal);
            requester.add(boldChunk);
            requester.add(normalChunk);
            document.add(requester);

            // Status
            Paragraph status = new Paragraph();
            boldChunk = new Chunk("Status: ");
            boldChunk.setFont(fontBold);
            normalChunk = new Chunk(demand.getDemandStatus());
            normalChunk.setFont(fontNormal);
            status.add(boldChunk);
            status.add(normalChunk);
            document.add(status);
            document.add(quebra);

            //Analista
            Paragraph analist = new Paragraph();
            boldChunk = new Chunk("Analista Responsavel: ");
            boldChunk.setFont(fontBold);
            normalChunk = new Chunk(demand.getClassification().getAnalistRegistry().getWorkerName());
            normalChunk.setFont(fontNormal);
            analist.add(boldChunk);
            analist.add(normalChunk);
            document.add(analist);
            document.add(quebra);

            // Situação atual
            Paragraph currentProblemTitle = new Paragraph(new Phrase(20F, "Situação Atual: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(currentProblemTitle);
            String currentProblem = demand.getCurrentProblem();
            currentProblem = currentProblem.replaceAll("&nbsp;", " ");
            org.jsoup.nodes.Document doc = Jsoup.parse(currentProblem);
            String currentProblemFinal = doc.text();
            Paragraph currentProblemAdd = new Paragraph(new Phrase(20F, currentProblemFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(currentProblemAdd);
            document.add(quebra);

            // Objetivo
            Paragraph objectiveTitle = new Paragraph(new Phrase(20F, "Objetivo: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(objectiveTitle);
            String objective = demand.getDemandObjective();
            objective = objective.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(objective);
            String objectiveFinal = doc.text();
            Paragraph objectiveAdd = new Paragraph(new Phrase(20F, objectiveFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(objectiveAdd);
            document.add(quebra);

//            // Benefício Real
//            Paragraph realBenefitTitle = new Paragraph(new Phrase(20F, "Benefício Real: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
//            document.add(realBenefitTitle);
//            Paragraph realBenefitMonthlyValue = new Paragraph(new Phrase(20F, demand.getRealBenefit().getRealCurrency()
//                    + " " + demand.getRealBenefit().getRealMonthlyValue(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
//            document.add(realBenefitMonthlyValue);
//            String realBenefitTitleDescription = demand.getRealBenefit().getRealBenefitDescription();
//            realBenefitTitleDescription = realBenefitTitleDescription.replaceAll("&nbsp;", " ");
//            doc = Jsoup.parse(realBenefitTitleDescription);
//            String realBenefitTitleDescripitionFinal = doc.text();
//            Paragraph realBenefitTitleDescripitionAdd = new Paragraph(new Phrase(20F, realBenefitTitleDescripitionFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
//            document.add(realBenefitTitleDescripitionAdd);
//            document.add(quebra);

            // Benefício Potencial
            Paragraph potentialBenefitTitle = new Paragraph(new Phrase(20F, "Benefício Potencial: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(potentialBenefitTitle);
            Paragraph potentialBenefitMonthlyValue = new Paragraph(new Phrase(20F, demand.getPotentialBenefit().getPotentialCurrency()
                    + " " + demand.getPotentialBenefit().getPotentialMonthlyValue(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(potentialBenefitMonthlyValue);

            String legalObrigation = "Sim";
            if (demand.getPotentialBenefit().getLegalObrigation() == false) {
                legalObrigation = "Não";
            }

            Paragraph paragraph = new Paragraph();
            paragraph.add(new Phrase(20F, "Obrigação legal: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            paragraph.add(new Phrase(20F, legalObrigation, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(paragraph);

            String potentialBenefitDescription = demand.getPotentialBenefit().getPotentialBenefitDescription();
            potentialBenefitDescription = potentialBenefitDescription.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(potentialBenefitDescription);
            String potentialBenefitDescriptionFinal = doc.text();
            Paragraph potentialBenefitDescriptionAdd = new Paragraph(new Phrase(20F, potentialBenefitDescriptionFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(potentialBenefitDescriptionAdd);
            document.add(quebra);

            // Benefício Qualitativo
            Paragraph qualitativeBenefitTitle = new Paragraph(new Phrase(20F, "Benefício Qualitativo: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(qualitativeBenefitTitle);
            String qualitativeBenefitDescription = demand.getQualitativeBenefit().getQualitativeBenefitDescription();
            qualitativeBenefitDescription = qualitativeBenefitDescription.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(qualitativeBenefitDescription);
            String qualitativeBenefitDescriptionFinal = doc.text();
            Paragraph qualitativeBenefitDescriptionAdd = new Paragraph(new Phrase(20F, qualitativeBenefitDescriptionFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(qualitativeBenefitDescriptionAdd);
            document.add(quebra);

            //Centros de custo (Tabela)
            Paragraph costCenterTitle = new Paragraph(new Phrase(20F, "Centros de Custo: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(costCenterTitle);
            document.add(quebra);

            PdfPTable tableCostCenter = new PdfPTable(2);
            tableCostCenter.setWidthPercentage(100);

            // Adicionar cabeçalho da tabela
            PdfPCell headerCell = new PdfPCell();
            headerCell.setPhrase(new Phrase("Centro de Custo", fontBold));
            tableCostCenter.addCell(headerCell);
            headerCell.setPhrase(new Phrase("Nome do Centro de Custo", fontBold));
            tableCostCenter.addCell(headerCell);

            // Adicionar linhas da tabela
            for (CostCenter costCenter : demand.getCostCenter()) {
                PdfPCell columnCell = new PdfPCell();
                columnCell.setPhrase(new Phrase(costCenter.getCostCenterCode() + "", fontNormal));
                tableCostCenter.addCell(columnCell);
                columnCell.setPhrase(new Phrase(costCenter.getCostCenter(), fontNormal));
                tableCostCenter.addCell(columnCell);
            }

            document.add(tableCostCenter);
            document.add(quebra);

            //Classificação
            Paragraph classificationTitle = new Paragraph(new Phrase(20F, "Classificação:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(classificationTitle);
            //Tamanho
            String size = demand.getClassification().getClassificationSize();
            size = size.replaceAll("&nbsp", " ");
            doc = Jsoup.parse(size);
            String sizeFinal = doc.text();
            Phrase sizeTitle = new Phrase(20F, "Tamanho: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
            Phrase sizeAdd = new Phrase(20F,sizeFinal, FontFactory.getFont(FontFactory.HELVETICA, 10));
            Phrase combined = new Phrase();
            combined.add(sizeTitle);
            combined.add(sizeAdd);
            document.add(combined);
            document.add(new Paragraph(""));

            //BU solicitante
            String requesterBU = demand.getClassification().getRequesterBu().getBu();
            requesterBU = requesterBU.replaceAll("&nbsp", " ");
            doc = Jsoup.parse(requesterBU);
            String requesterBUFinal = doc.text();
            Phrase requesterBUTitle = new Phrase(20F, "BU solicitante: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
            Phrase requesterBUAdd = new Phrase(20F, requesterBUFinal, FontFactory.getFont(FontFactory.HELVETICA, 10));
            combined = new Phrase();
            combined.add(requesterBUTitle);
            combined.add(requesterBUAdd);
            document.add(combined);
            document.add(new Paragraph(""));

            //BUs beneficiadas
            Paragraph beneficiariesBUsTitle = new Paragraph(new Phrase(20F, "BUs Beneficiadas:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(beneficiariesBUsTitle);
            List<Bu> requestersBUsList = demand.getClassification().getBeneficiaryBu();

            for (Bu bu : requestersBUsList) {
                String beneficiariesBUs = bu.getBu();
                beneficiariesBUs = beneficiariesBUs.replaceAll("&nbsp", " ");
                doc = Jsoup.parse(beneficiariesBUs);
                String beneficiariesBUsFinal = doc.text();
                Paragraph beneficiariesBUsAdd = new Paragraph(new Phrase(20F, "     " + beneficiariesBUsFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                document.add(beneficiariesBUsAdd);
            }
            //Sessão TI responsavel
            String itSection = demand.getClassification().getRequesterBu().getBu();
            itSection = itSection.replaceAll("&nbsp", " ");
            doc = Jsoup.parse(itSection);
            String itSectionFinal = doc.text();
            Phrase itSectionTitle = new Phrase(20F, "Sessão de TI Responsável: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
            Phrase itSectionAdd = new Phrase(20F, itSectionFinal, FontFactory.getFont(FontFactory.HELVETICA, 10));
            combined = new Phrase();
            combined.add(itSectionTitle);
            combined.add(itSectionAdd);
            document.add(combined);
            document.add(new Paragraph(""));


            //Codigo PPM
            String ppmCode = demand.getClassification().getPpmCode();
            ppmCode = ppmCode.replaceAll("&nbsp", " ");
            doc = Jsoup.parse(ppmCode);
            String ppmCodeFinal = doc.text();
            Phrase ppmCodeTitle =new Phrase(20F, "Código PPM:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
            Phrase ppmCodeAdd = new Phrase(20F, ppmCodeFinal, FontFactory.getFont(FontFactory.HELVETICA, 10));
            combined = new Phrase();
            combined.add(ppmCodeTitle);
            combined.add(ppmCodeAdd);
            document.add(combined);
            document.add(new Paragraph(""));

            //Link Epic Jira
            String linkJira = demand.getClassification().getEpicJiraLink();
            linkJira = linkJira.replaceAll("&nbsp", " ");
            doc = Jsoup.parse(linkJira);
            String linkJiraFinal = doc.text();
            Phrase linkJiraTitle = new Phrase(20F, "Link Epic Jira:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
            Phrase linkJiraAdd = new Phrase(20F, linkJiraFinal, FontFactory.getFont(FontFactory.HELVETICA, 10));
            combined = new Phrase();
            combined.add(linkJiraTitle);
            combined.add(linkJiraAdd);
            document.add(combined);
            document.add(quebra);

            //Despesas (Tabela)
            Paragraph expensesTitle = new Paragraph(new Phrase(20F, "Despesas: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(expensesTitle);
            document.add(quebra);

            PdfPTable tableExpenses = new PdfPTable(3);
            tableExpenses.setWidthPercentage(100);

            // Adicionar cabeçalho da tabela
            PdfPCell headerCellExpenses = new PdfPCell();
            headerCellExpenses.setPhrase(new Phrase("Perfil da despesa", fontBold));
            tableExpenses.addCell(headerCellExpenses);
            headerCellExpenses.setPhrase(new Phrase("Esforço", fontBold));
            tableExpenses.addCell(headerCellExpenses);
            headerCellExpenses.setPhrase(new Phrase("Valor hora", fontBold));
            tableExpenses.addCell(headerCellExpenses);


            // Adicionar linhas da tabela
            List<Expenses> expensesListProposal = expensesService.findAllByProposal(proposal);

            for (Expenses expenses: expensesListProposal) {
                PdfPCell columnCellExpenses = new PdfPCell();
                List<Expense> expenseList = expenses.getExpense();
                for(Expense expense : expenseList) {
                    if(expense.getExpenseType().equals("expenses")){
                        System.out.println(expense.getExpenseProfile());
                        columnCellExpenses.setPhrase(new Phrase(expense.getExpenseProfile(), fontNormal));
                        tableExpenses.addCell(columnCellExpenses);
                        columnCellExpenses.setPhrase(new Phrase(String.valueOf(expense.getAmountOfHours()), fontNormal));
                        tableExpenses.addCell(columnCellExpenses);
                        columnCellExpenses.setPhrase(new Phrase(String.valueOf(expense.getHourValue()), fontNormal));
                        tableExpenses.addCell(columnCellExpenses);
                    }
                }

            }
            //total e centro de custo

            Paragraph costCenter = new Paragraph();
            boldChunk = new Chunk("Centros de custo: ");
            boldChunk.setFont(fontBold);
            for (Expenses expenses: expensesListProposal) {
                List<ExpensesCostCenters> costCenterList = expenses.getExpensesCostCenters();
                for(ExpensesCostCenters costCenter1: costCenterList){
                    normalChunk = new Chunk(costCenter1.getCostCenter().getCostCenter());
                }

            }
            normalChunk.setFont(fontNormal);
            status.add(boldChunk);
            status.add(normalChunk);
            document.add(costCenter);
            document.add(quebra);

            PdfPCell columnCell = new PdfPCell();
//            columnCell.setPhrase(new Phrase(, fontNormal));
            tableCostCenter.addCell(columnCell);

            document.add(tableExpenses);
            document.add(quebra);


            //Investimentos/Recorrente (Tabela)

            //Recursos Internos (Tabela)
            Paragraph InternalResourcesTitle = new Paragraph(new Phrase(20F, "Recursos Internos: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(InternalResourcesTitle);
            document.add(quebra);

            PdfPTable tableInternalResources = new PdfPTable(4);
            tableInternalResources.setWidthPercentage(100);

            // Adicionar cabeçalho da tabela
            PdfPCell headerCellInternalResources = new PdfPCell();
            headerCellInternalResources.setPhrase(new Phrase("Perfil da despesa", fontBold));
            tableExpenses.addCell(headerCellInternalResources);
            headerCellInternalResources.setPhrase(new Phrase("Esforço", fontBold));
            tableExpenses.addCell(headerCellInternalResources);
            headerCellInternalResources.setPhrase(new Phrase("Valor hora", fontBold));
            tableExpenses.addCell(headerCellInternalResources);
            headerCellInternalResources.setPhrase(new Phrase("Centro de custo", fontBold));
            tableExpenses.addCell(headerCellInternalResources);

//            // Adicionar linhas da tabela
//            for (Expenses expenses: proposal.getExpensesList() ) {
//                PdfPCell columnCellExpenses = new PdfPCell();
//               List<Expense> expenseList = expenses.getExpense();
//               for(Expense expense : expenseList) {
//                   columnCellExpenses.setPhrase(new Phrase(expense.getExpenseProfile(), fontNormal));
//                   tableExpenses.addCell(columnCellExpenses);
//               }
//
//            }



            document.add(tableInternalResources);
            document.add(quebra);

            document.close();
        } catch (Exception e) {
            throw new IOException();
        }
        return null;
    }

    public void showText(Document document, PDPageContentStream contentStream) throws IOException {
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        document.select("br").append("\\n");

        for (int j = 0; j < document.text().length(); j++) {
            if (j + 1 == document.text().length()) {
                contentStream.showText(String.valueOf(document.text().charAt(j)));
                break;
            }
            if (j == 0) {
                contentStream.showText(String.valueOf(document.text().charAt(j)));
            } else {
                String n = document.text().charAt(j) + "" + document.text().charAt(j + 1);
                String n2 = document.text().charAt(j - 1) + "" + document.text().charAt(j);

                if (!n2.equals("\\n")) {
                    if (n.equals("\\n")) {
                        contentStream.newLineAtOffset(0, -20);
                    } else {
                        contentStream.showText(String.valueOf(document.text().charAt(j)));
                    }
                }
            }
        }
    }

    @GetMapping("/analyst/{workerCode}")
    public ResponseEntity<List<Proposal>> findAllByAnalyst(@PathVariable("workerCode") Worker workerCode) {
        List<Proposal> proposalList = proposalService.findAllByResponsibleAnalystWorkerCode(workerCode);
        return ResponseEntity.ok().body(proposalList);
    }

}
