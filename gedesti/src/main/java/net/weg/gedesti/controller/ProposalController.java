package net.weg.gedesti.controller;

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
import org.jsoup.nodes.Element;
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
    public void savePdf(@PathVariable(value = "proposalCode") Integer proposalCode, HttpServletResponse response) {
        try {
            Optional<Proposal> proposalOptional = proposalService.findById(proposalCode);
            Proposal proposal = proposalOptional.get();


            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float pageHeight = page.getMediaBox().getHeight();
            float margin = 100;
            float fontTitle = 12;
            float fontInformations = 10;
            float currentHeight = pageHeight - margin * 2;

            String path = new File(".").getCanonicalPath();
            PDImageXObject weg = PDImageXObject.createFromFile(path + "\\src\\main\\java\\net\\weg\\gedesti\\controller\\filePdf\\img.png", document);
            contentStream.drawImage(weg, 500, 730, 55, 40);


            contentStream.beginText();

            // Dados da demanda / proposta
            Demand demand = proposal.getDemand();
            contentStream.newLineAtOffset(60, 750);

            //Titulo
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
            Color color = Color.decode("#00579D");
            contentStream.setNonStrokingColor(color);
            contentStream.showText(demand.getDemandTitle() + " - " + demand.getDemandCode());
            contentStream.newLineAtOffset(0, -30);

            currentHeight -= (fontTitle + 60);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            //Data e hora
            Color color1 = Color.decode("#000000");
            contentStream.setNonStrokingColor(color1);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Data/Hora: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getDemandDate() + " - " + demand.getDemandHour() + "h");
            contentStream.newLineAtOffset(0, -20);

            currentHeight -= (fontInformations + 20);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            //Solicitante
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Solicitante: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getRequesterRegistration().getWorkerName());
            contentStream.newLineAtOffset(0, -20);

            currentHeight -= (fontInformations + 20);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            //Status
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Status: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getDemandStatus());
            contentStream.newLineAtOffset(0, -20);

            currentHeight -= (fontInformations + 20);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }
            //Analista Responsavel
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.showText("Analista responsável: ");
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.showText(proposal.getResponsibleAnalyst().getWorkerName());
            contentStream.newLineAtOffset(0, -40);

            currentHeight -= (fontInformations + 20);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            //Situação Atual
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Situação Atual ");
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);

            currentHeight -= (fontInformations + 40);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            float maxWidth = 500;
            float currentWidth = 0;

            String currentProblem = demand.getCurrentProblem();
            currentProblem = currentProblem.replaceAll("&nbsp;", " ");
            Document doc = Jsoup.parse(currentProblem);
            String currentProblemFinal = doc.text();
            StringBuilder lineBuilder = new StringBuilder();

            int textLength = 0;

            if (PDType1Font.HELVETICA.getStringWidth(currentProblemFinal) / 1000f * fontInformations <= maxWidth) {
                contentStream.showText(currentProblemFinal);
            } else {
                for (String word : currentProblemFinal.split(" ")) {
                    float wordWidth = PDType1Font.HELVETICA.getStringWidth(word) / 1000f * fontInformations;
                    textLength++;

                    if (currentWidth + wordWidth > maxWidth) {
                        currentHeight -= fontInformations;

                        if (currentHeight <= 0) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                            contentStream.newLineAtOffset(60, 750);
                            currentHeight = pageHeight - margin;
                        }

                        contentStream.showText(lineBuilder.toString());
                        contentStream.newLineAtOffset(0, -20);
                        lineBuilder.setLength(0);
                        currentWidth = 0;
                    } else if (currentProblemFinal.split(" ").length == textLength) {
                        contentStream.showText(lineBuilder.toString() + word);
                    }

                    lineBuilder.append(word).append(" ");
                    currentWidth += wordWidth + PDType1Font.HELVETICA.getStringWidth(" ") / 1000f * fontInformations;
                }
            }

            currentHeight -= (fontInformations * 3 + 20);

            //Objetivo
            contentStream.newLineAtOffset(0, -30);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Objetivo ");
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);

            currentHeight -= (fontInformations + 40);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            maxWidth = 500;
            currentWidth = 0;

            String objective = demand.getDemandObjective();
            objective = objective.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(objective);
            String objectiveFinal = doc.text();
            lineBuilder = new StringBuilder();

            textLength = 0;

            if (PDType1Font.HELVETICA.getStringWidth(objectiveFinal) / 1000f * fontInformations <= maxWidth) {
                contentStream.showText(objectiveFinal);
            } else {
                for (String word : objectiveFinal.split(" ")) {
                    float wordWidth = PDType1Font.HELVETICA.getStringWidth(word) / 1000f * fontInformations;
                    textLength++;

                    if (currentWidth + wordWidth > maxWidth) {
                        currentHeight -= fontInformations;

                        if (currentHeight <= 0) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                            contentStream.newLineAtOffset(60, 750);
                            currentHeight = pageHeight - margin;
                        }

                        contentStream.showText(lineBuilder.toString());
                        contentStream.newLineAtOffset(0, -20);
                        lineBuilder.setLength(0);
                        currentWidth = 0;
                    } else if (objectiveFinal.split(" ").length == textLength) {
                        contentStream.showText(lineBuilder.toString() + word);
                    }

                    lineBuilder.append(word).append(" ");
                    currentWidth += wordWidth + PDType1Font.HELVETICA.getStringWidth(" ") / 1000f * fontInformations;
                }
            }

            currentHeight -= (fontInformations * 3 + 20);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            // Benefício Real
            contentStream.newLineAtOffset(0, -30);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Benefício Real");
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getRealBenefit().getRealCurrency() + " " + demand.getRealBenefit().getRealMonthlyValue());
            contentStream.newLineAtOffset(0, -20);
            currentHeight -= (fontInformations + 40);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            maxWidth = 500;
            currentWidth = 0;

            String realBenefitDescription = demand.getRealBenefit().getRealBenefitDescription();
            realBenefitDescription = realBenefitDescription.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(realBenefitDescription);
            String realBenefitDescriptionFinal = doc.text();
            lineBuilder = new StringBuilder();

            textLength = 0;

            if (PDType1Font.HELVETICA.getStringWidth(realBenefitDescriptionFinal) / 1000f * fontInformations <= maxWidth) {
                contentStream.showText(realBenefitDescriptionFinal);
            } else {
                for (String word : realBenefitDescriptionFinal.split(" ")) {
                    float wordWidth = PDType1Font.HELVETICA.getStringWidth(word) / 1000f * fontInformations;
                    textLength++;

                    if (currentWidth + wordWidth > maxWidth) {
                        currentHeight -= fontInformations;

                        if (currentHeight <= 0) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                            contentStream.newLineAtOffset(60, 750);
                            currentHeight = pageHeight - margin;
                        }

                        contentStream.showText(lineBuilder.toString());
                        contentStream.newLineAtOffset(0, -20);
                        lineBuilder.setLength(0);
                        currentWidth = 0;
                    } else if (realBenefitDescriptionFinal.split(" ").length == textLength) {
                        contentStream.showText(lineBuilder.toString() + word);
                    }

                    lineBuilder.append(word).append(" ");
                    currentWidth += wordWidth + PDType1Font.HELVETICA.getStringWidth(" ") / 1000f * fontInformations;
                }
            }


            // Benefício Potencial
            contentStream.newLineAtOffset(0, -30);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Benefício Potencial");
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getPotentialBenefit().getPotentialCurrency() + " " + demand.getPotentialBenefit().getPotentialMonthlyValue());

            String legalObrigation = "Não";
            if (demand.getPotentialBenefit().getLegalObrigation() == true) {
                legalObrigation = "Sim";
            }
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("                  Obrigação legal: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(legalObrigation);
            contentStream.newLineAtOffset(0, -20);

            currentHeight -= (fontInformations + 40);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            maxWidth = 500;
            currentWidth = 0;

            String potentialBenefitDescription = demand.getPotentialBenefit().getPotentialBenefitDescription();
            potentialBenefitDescription = potentialBenefitDescription.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(potentialBenefitDescription);
            String potentialBenefitDescriptionFinal = doc.text();
            lineBuilder = new StringBuilder();

            textLength = 0;

            if (PDType1Font.HELVETICA.getStringWidth(potentialBenefitDescriptionFinal) / 1000f * fontInformations <= maxWidth) {
                contentStream.showText(potentialBenefitDescriptionFinal);
            } else {
                for (String word : potentialBenefitDescriptionFinal.split(" ")) {
                    float wordWidth = PDType1Font.HELVETICA.getStringWidth(word) / 1000f * fontInformations;
                    textLength++;

                    if (currentWidth + wordWidth > maxWidth) {
                        currentHeight -= fontInformations;

                        if (currentHeight <= 0) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                            contentStream.newLineAtOffset(60, 750);
                            currentHeight = pageHeight - margin;
                        }

                        contentStream.showText(lineBuilder.toString());
                        contentStream.newLineAtOffset(0, -20);
                        lineBuilder.setLength(0);
                        currentWidth = 0;
                    } else if (potentialBenefitDescriptionFinal.split(" ").length == textLength) {
                        contentStream.showText(lineBuilder.toString() + word);
                    }

                    lineBuilder.append(word).append(" ");
                    currentWidth += wordWidth + PDType1Font.HELVETICA.getStringWidth(" ") / 1000f * fontInformations;
                }
            }

            // Benefício Qualitativo
            contentStream.newLineAtOffset(0, -30);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Benefício Qualitativo");
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);

            currentHeight -= (fontInformations + 40);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            maxWidth = 500;
            currentWidth = 0;

            String qualitativeBenefitDescription = demand.getQualitativeBenefit().getQualitativeBenefitDescription();
            qualitativeBenefitDescription = qualitativeBenefitDescription.replaceAll("&nbsp;", " ");
            doc = Jsoup.parse(qualitativeBenefitDescription);
            String qualitativeBenefitDescriptionFinal = doc.text();
            lineBuilder = new StringBuilder();
            textLength = 0;

            if (PDType1Font.HELVETICA.getStringWidth(qualitativeBenefitDescriptionFinal) / 1000f * fontInformations <= maxWidth) {
                contentStream.showText(qualitativeBenefitDescriptionFinal);
            } else {
                for (String word : qualitativeBenefitDescriptionFinal.split(" ")) {
                    float wordWidth = PDType1Font.HELVETICA.getStringWidth(word) / 1000f * fontInformations;
                    textLength++;

                    if (currentWidth + wordWidth > maxWidth) {
                        currentHeight -= fontInformations;

                        if (currentHeight <= 0) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                            contentStream.newLineAtOffset(60, 750);
                            currentHeight = pageHeight - margin;
                        }

                        contentStream.showText(lineBuilder.toString());
                        contentStream.newLineAtOffset(0, -20);
                        lineBuilder.setLength(0);
                        currentWidth = 0;
                    } else if (qualitativeBenefitDescription.split(" ").length == textLength) {
                        contentStream.showText(lineBuilder.toString() + word);
                    }

                    lineBuilder.append(word).append(" ");
                    currentWidth += wordWidth + PDType1Font.HELVETICA.getStringWidth(" ") / 1000f * fontInformations;
                }
            }

            contentStream.newLineAtOffset(0, -20);

            String interalControlsRequirements = "Não";
            if (demand.getQualitativeBenefit().isInteralControlsRequirements() == true) {
                interalControlsRequirements = "Sim";
            }

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Requisitos de controle interno: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(interalControlsRequirements);

            // Centros de custos
            // Tabela
            contentStream.endText();
            contentStream.setStrokingColor(Color.BLACK);
            contentStream.setLineWidth(1);

            List<CostCenter> ListCostCenter = demand.getCostCenter();

            int initX = 60;
            int initY = (int) (currentHeight);
            int sizeHeight = 20;
            int sizeWidth = 246;
            int columns = 2;
            int lines = ListCostCenter.size();


            contentStream.addRect(initX, initY, sizeWidth, sizeHeight);
            contentStream.beginText();
            contentStream.newLineAtOffset(initX + 5, initY + 5);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Centro de custo");
            contentStream.endText();

            contentStream.addRect(initX + 246, initY, sizeWidth, sizeHeight);
            contentStream.beginText();
            contentStream.newLineAtOffset(initX + 246 + 5, initY + 5);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Nome do centro de custo");
            contentStream.endText();


            for (int i = 0; i < lines; i++) {
                initX = 60;
                initY -= sizeHeight;
                for (int j = 0; j < columns; j++) {
                    contentStream.addRect(initX, initY, sizeWidth, sizeHeight);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(initX + 5, initY + 5);
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);

                    if (j == 0) {
                        contentStream.showText(ListCostCenter.get(i).getCostCenterCode().toString());
                    } else {
                        contentStream.showText(ListCostCenter.get(i).getCostCenter().toString());
                    }
                    contentStream.endText();
                    initX += sizeWidth;
                }


                currentHeight -= (fontInformations);

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }

            }
            contentStream.stroke();

            //Classificação

            contentStream.beginText();
            if (!demand.getDemandStatus().equals("Backlog")) {
                currentHeight -= ((fontInformations + 20));

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }

                contentStream.newLineAtOffset(0, -20);
                contentStream.newLineAtOffset(initX - 500, initY);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText("Classificação");
                contentStream.newLineAtOffset(0, -20);
                currentHeight -= (fontInformations + 20);

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }
                contentStream.showText("Analista: ");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText(demand.getClassification().getAnalistRegistry().getWorkerName());
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                currentHeight -= (fontInformations + 20);

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }
                contentStream.showText("Tamanho: ");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText(demand.getClassification().getClassificationSize());
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                currentHeight -= (fontInformations + 20);

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.showText("Sessão de TI responsável: ");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText(demand.getClassification().getItSection());
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                currentHeight -= (fontInformations + 20);

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }
                contentStream.showText("BU Solicitante: ");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText(demand.getClassification().getRequesterBu().getBu());
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                currentHeight -= (fontInformations + 20);

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }
                contentStream.showText("BUs Beneficiadas: ");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                List<Bu> requestersBUsList = demand.getClassification().getBeneficiaryBu();

                for (Bu bu : requestersBUsList) {
                    currentHeight -= (fontInformations + 20);

                    if (currentHeight <= 0) {
                        contentStream.endText();
                        contentStream.close();

                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                        contentStream.newLineAtOffset(60, 750);
                        currentHeight = pageHeight - margin;
                    }

                    contentStream.showText(bu.getBu());
                }

                currentHeight -= (fontInformations + 20) * 3;

                if (currentHeight <= 0) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                    contentStream.newLineAtOffset(60, 750);
                    currentHeight = pageHeight - margin;
                }

                if (!demand.getDemandStatus().equals("BacklogRanked") || demand.getDemandStatus().equals("BacklogRankApproved")) {
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    contentStream.showText("Código PPM: ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.showText(demand.getClassification().getPpmCode());

                    contentStream.newLineAtOffset(0, -20);
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                    contentStream.showText("Link Epic do Jira: ");
                    contentStream.setFont(PDType1Font.HELVETICA, 10);
                    contentStream.showText(demand.getClassification().getEpicJiraLink());
                }
            }

            currentHeight -= (40);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            // Dados da proposta
            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);

            contentStream.showText("Payback: " );
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.showText(proposal.getPayback()+ "meses");
            contentStream.newLineAtOffset(0, -20);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date currentDate = proposal.getInitialRunPeriod();
            String formattedInitialDate = dateFormat.format(currentDate);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.showText("Período inicial de execução: " );
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.showText(formattedInitialDate);

            currentHeight -= (40);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            contentStream.newLineAtOffset(0, -20);
            currentDate = proposal.getFinalExecutionPeriod();
            String formattedFinalDate = dateFormat.format(currentDate);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.showText("Perído final de execução: " );
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.showText(formattedFinalDate);

            currentHeight -= (20);

            if (currentHeight <= 0) {
                contentStream.endText();
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                contentStream.newLineAtOffset(60, 750);
                currentHeight = pageHeight - margin;
            }

            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.showText("Escopo do projeto: ");
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            contentStream.newLineAtOffset(0, -20);
            Document descriptive = Jsoup.parse(proposal.getDescriptiveProposal());
            showText(descriptive, contentStream);


            contentStream.newLineAtOffset(0, -20);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.showText("Responsáveis pelo negócio: ");
            List<Worker> responsibleForBusiness = proposal.getWorkers();
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            for (Worker worker : responsibleForBusiness) {
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(worker.getWorkerCode() + " - " + worker.getWorkerName());
            }

//            contentStream.newLineAtOffset(0, -20);
//            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//            contentStream.showText("Custos de Execução: ");
//            contentStream.setFont(PDType1Font.HELVETICA, 10);
//            List<Expenses> expensesList = expensesService.findAllByProposalProposalCode(proposal.getProposalCode());
//
//            for (Expenses expenses : expensesList) {
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText(expenses.getExpensesType());
//                contentStream.newLineAtOffset(0, -20);
//
//                List<Expense> expenseList = expenses.getExpense();
//                List<ExpensesCostCenters> expensesCostCentersList = expenses.getExpensesCostCenters();
//
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//                contentStream.showText("DESPESAS");
//                for (Expense expense : expenseList) {
//                    contentStream.newLineAtOffset(0, -20);
//                    contentStream.showText("Perfil da despesa: " );
//                    contentStream.setFont(PDType1Font.HELVETICA, 10);
//                    contentStream.showText(expense.getExpenseProfile());
//                    contentStream.newLineAtOffset(0, -20);
//                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//                    contentStream.showText("Quantidade de horas: " );
//                    contentStream.setFont(PDType1Font.HELVETICA, 10);
//                    contentStream.showText(expense.getAmountOfHours() + "h");
//                    contentStream.newLineAtOffset(0, -20);
//                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//                    contentStream.showText("Valor hora: R$" );
//                    contentStream.setFont(PDType1Font.HELVETICA, 10);
//                    contentStream.showText(String.valueOf(expense.getHourValue()));
//                    contentStream.newLineAtOffset(0, -20);
//                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//                    contentStream.showText("Valor total: R$" );
//                    contentStream.setFont(PDType1Font.HELVETICA, 10);
//                    contentStream.showText(String.valueOf(expense.getTotalValue()));
//                }
//
//                for (ExpensesCostCenters expensesCostCenters : expensesCostCentersList) {
//                    contentStream.newLineAtOffset(0, -20);
//                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//                    contentStream.showText("Centro de custo: ");
//                    contentStream.setFont(PDType1Font.HELVETICA, 10);
//                    contentStream.showText(expensesCostCenters.getCostCenter().getCostCenter() + " - " + expensesCostCenters.getPercent()+ "%");
//                }
//            }
//
//            if (!proposal.getProposalStatus().equals("Pending")) {
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//                contentStream.showText("Opinião da comissão: ");
//                contentStream.setFont(PDType1Font.HELVETICA, 10);
//                contentStream.showText(proposal.getCommissionOpinion());
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
//                contentStream.showText("Proposta publicada: " );
//                contentStream.setFont(PDType1Font.HELVETICA, 10);
//                contentStream.showText(String.valueOf(proposal.getPublished()));
//            }


            contentStream.endText();
            contentStream.setStrokingColor(Color.BLACK);
            contentStream.setLineWidth(1);

            List<Expenses> expensesList = expensesService.findAllByProposalProposalCode(proposal.getProposalCode());

            initX = 60;
            initY = (int) (currentHeight - 180);
            sizeHeight = 20;
            sizeWidth = 246;
            columns = 2;

            for (Expenses expenses : expensesList) {
                // Cabeçalho da tabela
                contentStream.beginText();
                if(expenses.getExpensesType().equals("expenses")){
                    contentStream.newLineAtOffset(initX + 5, initY + 5);
                } else {
                    contentStream.newLineAtOffset(65, initY + 15);
                }
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
                contentStream.showText(expenses.getExpensesType());
                contentStream.newLineAtOffset(initX, 50);
                contentStream.endText();



                contentStream.addRect(initX, initY, sizeWidth, sizeHeight);
                contentStream.beginText();
                contentStream.newLineAtOffset(initX + 5, initY + 50);

                if(expenses.getExpensesType().equals("expenses")){
                    contentStream.newLineAtOffset(initX + 5, initY + 50);
                } else {
                    contentStream.newLineAtOffset(65, initY + 50);
                }

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
                contentStream.showText("Nome da despesa");
                contentStream.endText();

                contentStream.addRect(initX + 246, initY, sizeWidth, sizeHeight);
                contentStream.beginText();
                contentStream.newLineAtOffset(initX + 246, initY + 5);

                if(expenses.getExpensesType().equals("expenses")){
                    contentStream.newLineAtOffset(initX + 246, initY + 5);
                } else {
                    contentStream.newLineAtOffset(initX + 246, initY + 5);
                }


                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
                contentStream.showText("Esforço");
                contentStream.endText();

                contentStream.addRect(initX + 280, initY, sizeWidth, sizeHeight);
                contentStream.beginText();
                contentStream.newLineAtOffset(initX + 280 + 5, initY + 5);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
                contentStream.showText("Valor Total");
                contentStream.endText();

                // Preencher a tabela com as despesas
                List<Expense> expenseList = expenses.getExpense();
                lines = expenseList.size();
                initY -= sizeHeight;

                for (int i = 0; i < lines; i++) {
                    initX = 60;
                    initY -= sizeHeight;

                    for (int j = 0; j < columns; j++) {
                        contentStream.addRect(initX, initY, sizeWidth, sizeHeight);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(initX + 5, initY + 5);
                        contentStream.setFont(PDType1Font.HELVETICA, fontInformations);

                        if (j == 0) {
                            contentStream.showText(expenses.getExpensesType());
                        } else {
                            contentStream.showText(expenseList.get(i).getExpenseProfile());
                        }
                        contentStream.endText();
                        initX += sizeWidth;
                    }

                    currentHeight -= (fontInformations);

                    if (currentHeight <= 0) {
                        contentStream.endText();
                        contentStream.close();

                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
                        contentStream.newLineAtOffset(60, 750);
                        currentHeight = pageHeight - margin;
                    }
                }

                contentStream.stroke();
            }



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
