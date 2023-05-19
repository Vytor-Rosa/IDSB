package net.weg.gedesti.controller;

import com.lowagie.text.DocumentException;
import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.AgendaDTO;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.dto.ProposalDTO;
import net.weg.gedesti.model.entity.*;
import net.weg.gedesti.model.service.ExpenseService;
import net.weg.gedesti.model.service.ExpensesService;
import net.weg.gedesti.model.service.ProposalService;
import net.weg.gedesti.repository.ExpenseRepository;
import net.weg.gedesti.repository.ProposalRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.*;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/proposal")
public class ProposalController {
    private ProposalService proposalService;
    private ProposalRepository proposalRepository;
    private ExpensesService expensesService;

    @GetMapping
    public ResponseEntity<List<Proposal>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(proposalService.findAll());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Proposal>> findAll(@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.status(HttpStatus.FOUND).body(proposalService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid ProposalDTO proposalDTO) {
        Proposal proposal = new Proposal();
        BeanUtils.copyProperties(proposalDTO, proposal);
        return ResponseEntity.status(HttpStatus.CREATED).body(proposalService.save(proposal));
    }

    @GetMapping("/{proposalCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "proposalCode") Integer proposalCode) throws IOException {
        Optional<Proposal> proposalOptional = proposalService.findById(proposalCode);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No proposal with code: " + proposalCode);
        }
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

    @GetMapping("/demand/{demand}")
    public ResponseEntity<Object> findByDemand(@PathVariable(value = "demand") Demand demand) {
        Optional<Proposal> proposalOptional = proposalService.findByDemand(demand);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demand " + demand + " doesn't exists");
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(proposalOptional);
        }
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
    public ResponseEntity<Object> findByDemandCode(@PathVariable(value = "demandCode") Demand demandCode) {
        Optional<Proposal> proposalOptional = proposalService.findByDemandDemandCode(demandCode);
        if (proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demand " + demandCode + " doesn't exists");
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).body(proposalOptional);
        }
    }

    public void savePdf(final Proposal proposal) throws IOException {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
            contentStream.beginText();

            // Dados da demanda / proposta
            Demand demand = proposal.getDemand();

            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(proposal.getProposalName() + " - " + proposal.getProposalCode());
            contentStream.newLineAtOffset(-50, -20);
            contentStream.showText("Status: " + proposal.getProposalStatus());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Data: " + proposal.getProposalDate());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Solicitante: " + demand.getRequesterRegistration().getWorkerName());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Analista Responsável: " + proposal.getResponsibleAnalyst().getWorkerName());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Situação atual: " + proposal.getDemand().getCurrentProblem());
//            String currentProblem = proposal.getDemand().getCurrentProblem();
//            currentProblem = currentProblem.replaceAll("&nbsp;", " ");
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocumentFromString("<html><head></head><body><div>" + currentProblem + "</div></body></html>");
//            renderer.layout();
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            String renderedContent = baos.toString();
//            System.out.println(renderedContent);

//            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
//                renderer.createPDF(baos);
//                baos.flush();
//                try (InputStream is = new ByteArrayInputStream(baos.toByteArray())) {
//                    PDDocument pdfDocument = PDDocument.load(is);
//                    if (pdfDocument.getNumberOfPages() > 0) {
//                        PDPage pagee = pdfDocument.getPage(0);
//                        document.addPage(pagee);
//                    }
//                }
//            }

            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Objetivo: " + demand.getDemandObjective());

            // Benefício Real
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("BENEFÍCIO REAL");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Valor mensal: " + demand.getRealBenefit().getRealCurrency() + " " + proposal.getDemand().getRealBenefit().getRealMonthlyValue());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Descrição: " + demand.getRealBenefit().getRealBenefitDescription());

            // Benefício Potencial
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("BENEFÍCIO POTENCIAL");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Valor mensal: " + demand.getPotentialBenefit().getPotentialCurrency() + " " + demand.getPotentialBenefit().getPotentialMonthlyValue());
            contentStream.newLineAtOffset(0, -20);

            String legalObrigation = "Não";
            if (demand.getPotentialBenefit().getLegalObrigation() == true) {
                legalObrigation = "Sim";
            }

            contentStream.showText("Obrigação legal: " + legalObrigation);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Descrição: " + demand.getPotentialBenefit().getPotentialBenefitDescription());

            // Benefício Qualitativo
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("BENEFÍCIO QUALITATIVO");
            contentStream.newLineAtOffset(0, -20);

            String interalControlsRequirements = "Não";
            if (demand.getQualitativeBenefit().isInteralControlsRequirements() == true) {
                interalControlsRequirements = "Sim";
            }

            contentStream.showText("Requisitos de controle interno: " + interalControlsRequirements);
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Descrição: " + demand.getQualitativeBenefit().getQualitativeBenefitDescription());

            // Centros de custos
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("CENTRO DE CUSTO");
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Centro de custo               Nome do centro de custo");

            List<CostCenter> costCenterList = demand.getCostCenter();

            for (CostCenter costCenter : costCenterList) {
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(costCenter.getCostCenterCode() + "           " + costCenter.getCostCenter());
            }

            // Classificação
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Tamanho: " + demand.getClassification().getClassificationSize());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Sessão de TI responsável: " + demand.getClassification().getItSection());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("BU Solicitante: " + demand.getClassification().getRequesterBu().getBu());
            contentStream.newLineAtOffset(0, -20);

            contentStream.showText("BUs Beneficiadas: ");
            List<Bu> requestersBUsList = demand.getClassification().getBeneficiaryBu();

            for (Bu bu : requestersBUsList) {
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(bu.getBu());
            }

            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Código PPM: " + demand.getClassification().getPpmCode());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Link Epic do Jira: " + demand.getClassification().getEpicJiraLink());

            // Dados da proposta
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Payback: " + proposal.getPayback());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Período inicial de execução: " + proposal.getInitialRunPeriod());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Perído final de execução: " + proposal.getFinalExecutionPeriod());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Escopo do projeto: " + proposal.getDescriptiveProposal());
            contentStream.newLineAtOffset(0, -20);

            contentStream.showText("RESPONSÁVEIS PELO NEGÓCIO");
            List<Worker> responsibleForBusiness = proposal.getWorkers();

            for (Worker worker : responsibleForBusiness) {
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(worker.getWorkerCode() + " - " + worker.getWorkerName());
            }

            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("CUSTOS DE EXECUÇÃO");

//            List<Expenses> expensesList = proposal.getExpensesList();
//            System.out.println(expensesList);

            if (!proposal.getProposalStatus().equals("Pending")) {
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Opinião da comissão: " + proposal.getCommissionOpinion());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Proposta publicada: " + proposal.getPublished());
            }

            contentStream.endText();
            contentStream.close();

            document.save(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Downloads\\" + proposal.getProposalCode() + " - " + proposal.getProposalName() + ".pdf"));
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
