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
        for(Proposal proposal : proposals) {
            if(proposal.getScore() == null) {
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

            Paragraph title = new Paragraph(new Phrase(20F, demand.getDemandTitle(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, baseColor)));
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

            // Benefício Real
            Paragraph realBenefitTitle = new Paragraph(new Phrase(20F, "Benefício Real: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
            document.add(realBenefitTitle);
            Paragraph realBenefitMonthlyValue = new Paragraph(new Phrase(20F, demand.getRealBenefit().getRealCurrency()
                    + " " + demand.getRealBenefit().getRealMonthlyValue(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(realBenefitMonthlyValue);
            String realBenefitTitleDescription = demand.getRealBenefit().getRealBenefitDescription();
            realBenefitTitleDescription = realBenefitTitleDescription.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(realBenefitTitleDescription);
            String realBenefitTitleDescripitionFinal = doc.text();
            Paragraph realBenefitTitleDescripitionAdd = new Paragraph(new Phrase(20F, realBenefitTitleDescripitionFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(realBenefitTitleDescripitionAdd);
            document.add(quebra);

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
            Paragraph potentialBenefitLegalObrigation = new Paragraph(new Phrase(20F, "Obrigação legal: "
                    + legalObrigation, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(potentialBenefitLegalObrigation);

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

            // Centros de custo
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

            //

            document.add(tableCostCenter);
            document.close();
        } catch (Exception e) {
            throw new IOException();
        }
        return null;
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

    @GetMapping("/analyst/{workerCode}")
    public ResponseEntity<List<Proposal>> findAllByAnalyst(@PathVariable("workerCode") Worker workerCode) {
        List<Proposal> proposalList = proposalService.findAllByResponsibleAnalystWorkerCode(workerCode);
        return ResponseEntity.ok().body(proposalList);
    }

}
