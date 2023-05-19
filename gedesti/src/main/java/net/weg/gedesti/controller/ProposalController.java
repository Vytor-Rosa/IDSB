package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.AgendaDTO;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.dto.ProposalDTO;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.service.ProposalService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/proposal")
public class ProposalController {

    private ProposalService proposalService;
    private ProposalRepository proposalRepository;

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
    public ResponseEntity<Object> findById(@PathVariable(value = "proposalCode") Integer proposalCode) {
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
        if(proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demand " + demand + " doesn't exists");
        }else{
            return ResponseEntity.status(HttpStatus.FOUND).body(proposalOptional);
        }
    }

    @Modifying
    @Transactional
    @PutMapping("/status/{proposalCode}")
    public  ResponseEntity<Object> addOpinion(@PathVariable(value = "proposalCode") Integer proposalCode, @RequestBody ProposalDTO proposalDTO){
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
    public  ResponseEntity<Object> updatePublish(@PathVariable(value = "proposalCode") Integer proposalCode, @RequestBody ProposalDTO proposalDTO){
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
        if(proposalOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demand " + demandCode + " doesn't exists");
        }else{
            return ResponseEntity.status(HttpStatus.FOUND).body(proposalOptional);
        }
    }

    public void savePdf(final Proposal proposal) throws IOException {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();

            // Dados gerais da proposta
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(proposal.getProposalName() + " - " + proposal.getProposalCode());
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Solicitante: " + demand.getRequesterRegistration().getWorkerName());
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Data: " + demand.getDemandDate() + " - " + demand.getDemandHour() + "h");
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Situação atual: " + demand.getCurrentProblem());
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Objetivo: " + demand.getDemandObjective());
//
//            // Benefício Real
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("BENEFÍCIO REAL");
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Valor mensal: " + demand.getRealBenefit().getRealCurrency() + " " + demand.getRealBenefit().getRealMonthlyValue());
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Descrição: " + demand.getRealBenefit().getRealBenefitDescription());
//
//            // Benefício Potencial
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("BENEFÍCIO POTENCIAL");
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Valor mensal: " + demand.getPotentialBenefit().getPotentialCurrency() + " " + demand.getPotentialBenefit().getPotentialMonthlyValue());
//            contentStream.newLineAtOffset(0, -20);
//
//            String legalObrigation = "Não";
//            if (demand.getPotentialBenefit().getLegalObrigation() == true) {
//                legalObrigation = "Sim";
//            }
//
//            contentStream.showText("Obrigação legal: " + legalObrigation);
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Descrição: " + demand.getPotentialBenefit().getPotentialBenefitDescription());
//
//            // Benefício Qualitativo
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("BENEFÍCIO QUALITATIVO");
//            contentStream.newLineAtOffset(0, -20);
//
//            String interalControlsRequirements = "Não";
//            if (demand.getQualitativeBenefit().isInteralControlsRequirements() == true) {
//                interalControlsRequirements = "Sim";
//            }
//
//            contentStream.showText("Requisitos de controle interno: " + interalControlsRequirements);
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Descrição: " + demand.getQualitativeBenefit().getQualitativeBenefitDescription());
//
//            // Centros de custos
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("CENTRO DE CUSTO");
//            contentStream.newLineAtOffset(0, -20);
//            contentStream.showText("Centro de custo               Nome do centro de custo");
//
//            List<CostCenter> costCenterList = demand.getCostCenter();
//
//            for (CostCenter costCenter : costCenterList) {
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText(costCenter.getCostCenterCode() + "           " + costCenter.getCostCenter());
//            }

//            contentStream.endText();
//            contentStream.close();
//
//            document.save(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Downloads\\" + demand.getDemandCode() + " - " + demand.getDemandTitle() + ".pdf"));
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
