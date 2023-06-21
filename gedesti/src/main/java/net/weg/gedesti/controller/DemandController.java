package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.model.entity.Bu;
import net.weg.gedesti.model.entity.CostCenter;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Worker;
import net.weg.gedesti.model.service.ClassificationService;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.model.service.WorkerService;
import net.weg.gedesti.repository.DemandRepository;
import net.weg.gedesti.util.DemandUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/demand")
public class DemandController {
    private DemandService demandService;
    private WorkerService workerService;
    private ClassificationService classificationService;
    private DemandRepository demandRepository;

    @GetMapping
    public ResponseEntity<List<Demand>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findAll());
    }

    public List<Demand> findByStatus(String value) {
        List<Demand> demandList = demandService.findAll();
        List<Demand> demandsStatus = new ArrayList<>();
        for (Demand demand : demandList) {
            if (demand.getDemandStatus().trim().toLowerCase().contains(value.trim().toLowerCase())) {
                demandsStatus.add(demand);
            }
        }
        return demandsStatus;
    }

    @GetMapping("/pdf/{demandCode}")
    public ResponseEntity<Object> savePdf(@PathVariable(value = "demandCode") Integer demandCode, HttpServletResponse response) throws IOException {
        try {
            List<Demand> demandOptional = demandService.findByDemandCode(demandCode);
            Demand demand = new Demand();

            for (Demand demands : demandOptional) {
                if (demands.getActiveVersion() == true) {
                    demand = demands;
                }
            }

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

            // Dados gerais da demanda
            contentStream.beginText();
            contentStream.newLineAtOffset(60, 750);

            //Titulo
            contentStream.newLineAtOffset(0,-20);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontTitle);
            Color color = Color.decode("#00579D");
            contentStream.setNonStrokingColor(color);
            String titulo=demand.getDemandTitle() + " - " + demand.getDemandCode();
            contentStream.newLineAtOffset(0, -30);

            float maxWidth = 500;
            float currentWidth = 0;
            Integer textLength = 0;
            StringBuilder lineBuilder = new StringBuilder();
            if (PDType1Font.HELVETICA.getStringWidth(titulo) / 1000f * fontTitle <= maxWidth) {
                contentStream.showText(titulo);
            } else {
                for (String word : titulo.split(" ")) {
                    float wordWidth = PDType1Font.HELVETICA.getStringWidth(word) / 1000f * fontTitle;
                    textLength++;

                    if (currentWidth + wordWidth > maxWidth) {
                        currentHeight -= fontTitle;

                        if (currentHeight <= 0) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            contentStream.beginText();
                            contentStream.setFont(PDType1Font.HELVETICA, fontTitle);
                            contentStream.newLineAtOffset(60, 750);
                            currentHeight = pageHeight - margin;
                        }

                        contentStream.showText(lineBuilder.toString());
                        contentStream.newLineAtOffset(0, -20);
                        lineBuilder.setLength(0);
                        currentWidth = 0;
                    } else if (titulo.split(" ").length == textLength) {
                        contentStream.showText(lineBuilder.toString() + word);
                    }

                    lineBuilder.append(word).append(" ");
                    currentWidth += wordWidth + PDType1Font.HELVETICA.getStringWidth(" ") / 1000f * fontTitle;
                }
            }

            //Data e hora
            contentStream.newLineAtOffset(0,-20);
            Color color1 = Color.decode("#000000");
            contentStream.setNonStrokingColor(color1);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Data/Hora: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getDemandDate() + " - " + demand.getDemandHour() + "h");
            contentStream.newLineAtOffset(0, -20);

            //Solicitante
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Solicitante: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getRequesterRegistration().getWorkerName());
            contentStream.newLineAtOffset(0, -20);

            //Status
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontInformations);
            contentStream.showText("Status: ");
            contentStream.setFont(PDType1Font.HELVETICA, fontInformations);
            contentStream.showText(demand.getDemandStatus());
            contentStream.newLineAtOffset(0, -40);

            // VERIFICA O TAMANHO DO TITULO, DATA, SOLICITANTE E STATUS
            currentHeight -= (fontTitle + 60 + (fontInformations * 3) + (20 * 3));

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

            maxWidth = 500;
            currentWidth = 0;

            String currentProblem = demand.getCurrentProblem();
            currentProblem = currentProblem.replaceAll("&nbsp;", " ");
            Document doc = Jsoup.parse(currentProblem);
            String currentProblemFinal = doc.text();
            lineBuilder = new StringBuilder();
            textLength = 0;

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
            int initY = (int) (currentHeight );
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

                contentStream.newLineAtOffset(0,-20);
                contentStream.newLineAtOffset(initX -500, initY);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                contentStream.newLineAtOffset(0, 0);
                contentStream.showText("Classificação");
                contentStream.newLineAtOffset(0, -20);
                currentHeight -= (fontInformations+20);

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
                currentHeight -= (fontInformations+20);

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
                currentHeight -= (fontInformations+20);

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
                contentStream.setFont(PDType1Font.HELVETICA_BOLD,10);
                contentStream.showText("Sessão de TI responsável: ");
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.showText(demand.getClassification().getItSection());
                contentStream.newLineAtOffset(0, -20);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
                currentHeight -= (fontInformations+20);

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
                currentHeight -= (fontInformations+20);

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

            return ResponseEntity.ok().build();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class ImageIOHelper {
        static byte[] writeImageToByteArray(BufferedImage image, String format) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }
    }

    @PostMapping("/excel")
    public void saveExcel(final String attachmentName, final List<Demand> demands) throws IOException {
        try (var workbook = new XSSFWorkbook();
             var outputStream = new FileOutputStream(attachmentName)) {

            CellStyle style = workbook.createCellStyle();

            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setWrapText(false);

            CellStyle bodyStyle = workbook.createCellStyle();
            bodyStyle.setBorderBottom(BorderStyle.THIN);
            bodyStyle.setBorderTop(BorderStyle.THIN);
            bodyStyle.setBorderRight(BorderStyle.THIN);
            bodyStyle.setBorderLeft(BorderStyle.THIN);
            bodyStyle.setAlignment(HorizontalAlignment.CENTER);
            bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            bodyStyle.setWrapText(false);

            var sheet = workbook.createSheet();
            sheet.setColumnWidth(1, 30 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 23 * 256);
            sheet.setColumnWidth(5, 20 * 256);
            sheet.setColumnWidth(6, 25 * 256);
            sheet.setColumnWidth(7, 25 * 256);
            sheet.setColumnWidth(8, 25 * 256);
            sheet.setColumnWidth(9, 25 * 256);
            sheet.setColumnWidth(10, 25 * 256);
            sheet.setColumnWidth(11, 20 * 256);
            sheet.setColumnWidth(12, 30 * 256);
            sheet.setColumnWidth(13, 65 * 256);

            int rowNum = 0;
            var row = sheet.createRow(rowNum);
            var cell = row.createCell(0);
            sheet.autoSizeColumn(0);
            cell.setCellStyle(style);
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue("Código");
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue("Titulo");
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue("Data de Criação");
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue("Status");
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("Responsável");
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue("Objetivo");
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue("Centro de Custos");
            cell.setCellStyle(style);
            cell = row.createCell(7);
            cell.setCellValue("Beneficio Potencial");
            cell.setCellStyle(style);
            cell = row.createCell(8);
            cell.setCellValue("Beneficio Real");
            cell.setCellStyle(style);
            cell = row.createCell(9);
            cell.setCellValue("Código PPM");
            cell.setCellStyle(style);
            cell = row.createCell(10);
            cell.setCellValue("Link Epic Jira");
            cell.setCellStyle(style);
            cell = row.createCell(11);
            cell.setCellValue("Tamanho");
            cell.setCellStyle(style);
            cell = row.createCell(12);
            cell.setCellValue("Bu Solicitante");
            cell.setCellStyle(style);
            cell = row.createCell(13);
            cell.setCellValue("Sessão TI Responsável");
            cell.setCellStyle(style);
            for (Demand demand : demands) {
                int index = 0;
                row = sheet.createRow(rowNum++);
                cell = row.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getDemandCode());
                cell = row.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getDemandTitle());
                cell = row.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getDemandDate());
                cell = row.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getDemandStatus());
                cell = row.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getRequesterRegistration().getWorkerName());
                cell = row.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getDemandObjective());
                cell = row.createCell(6);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getCostCenter().get(index).getCostCenter());
                cell = row.createCell(7);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getPotentialBenefit().getPotentialCurrency() + " " + demand.getPotentialBenefit().getPotentialMonthlyValue());
                cell = row.createCell(8);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(demand.getRealBenefit().getRealCurrency() + " " + demand.getRealBenefit().getRealMonthlyValue());
                if (demand.getClassification() != null) {
                    cell = row.createCell(9);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(demand.getClassification().getPpmCode());
                    cell = row.createCell(10);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(demand.getClassification().getEpicJiraLink());
                    cell = row.createCell(11);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(demand.getClassification().getClassificationSize());
                    cell = row.createCell(12);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(demand.getClassification().getRequesterBu().getBu());
                    cell = row.createCell(13);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(demand.getClassification().getItSection());
                } else {
                    cell = row.createCell(9);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("");
                    cell = row.createCell(10);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("");
                    cell = row.createCell(11);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("");
                    cell = row.createCell(12);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("");
                    cell = row.createCell(13);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("");
                }
                index++;
            }

            workbook.write(outputStream);
            outputStream.close();
            openFile(attachmentName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openFile(String attachmentName) throws IOException {
        try {
            File file = new File("C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\GitHub\\IDSB\\gedesti\\" + attachmentName);
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", "start", "", file.toString());
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/historical/{demandCode}")
    public ResponseEntity<List<Demand>> historical(@PathVariable(value = "demandCode") Integer demandCode) {
        return ResponseEntity.status(HttpStatus.OK).body(demandService.findAllByDemandCode(demandCode));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(value = "demand") @Valid String
                                               demandJson, @RequestParam(value = "demandAttachments", required = false) List<MultipartFile> demandAttachment) {
        DemandUtil demandUtil = new DemandUtil();
        Demand demand = demandUtil.convertJsonToModel(demandJson);
        demand.setDemandVersion(1);
        demand.setDemandHour(LocalTime.now());

        LocalDate createDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        String formattedString = createDate.format(formatter);

        demand.setDemandDate(formattedString);

        List<Demand> demands = demandRepository.findAllByActiveVersion();
        Integer size = demands.size();
        demand.setDemandCode(size + 1);
        demand.setActiveVersion(true);

        if (demandAttachment != null) {
            demand.setDemandAttachment(demandAttachment);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(demandService.save(demand));
    }

    @GetMapping("/{demandCode}")
    public ResponseEntity<Object> findByDemandCode(@PathVariable(value = "demandCode") Integer demandCode) throws
            IOException {
        List<Demand> demandOptional = demandService.findByDemandCode(demandCode);

        for (Demand demand : demandOptional) {
            if (demand.getActiveVersion() == true) {
                return ResponseEntity.status(HttpStatus.OK).body(demand);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("OUT");
    }

    @DeleteMapping("/{demandCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "demandCode") Integer demandCode) {
        Optional<Demand> demandOptional = demandService.findById(demandCode);
        if (demandOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No demand with code: " + demandCode);
        }
        demandService.deleteById(demandCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Demand " + demandCode + " successfully deleted!");
    }

    @Modifying
    @Transactional
    @PutMapping("/{demandCode}")
    public ResponseEntity<Object> update(@RequestParam(value = "demand") @Valid String demandJson,
                                         @PathVariable(value = "demandCode") Integer demandCode,
                                         @RequestParam(value = "demandAttachment", required = false) List<MultipartFile> demandAttachment) throws
            IOException {
        DemandUtil demandUtil = new DemandUtil();
        Demand demand = demandUtil.convertJsonToModel(demandJson);
        if (demandAttachment != null) {
            demand.setDemandAttachment(demandAttachment);
        }

        demand.setDemandCode(demandCode);
        Integer maxVersion = 0;
        List<Demand> demandList = demandService.findAll();
        for (int i = 0; i < demandList.size(); i++) {
            if (demandList.get(i).getDemandCode() == demandCode) {
                if (demandList.get(i).getDemandVersion() > maxVersion) {
                    demandList.get(i).setActiveVersion(false);
                    maxVersion = demandList.get(i).getDemandVersion();
                }
            }
        }
        demand.setDemandHour(LocalTime.now());
        LocalDate createDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        String formattedString = createDate.format(formatter);
        demand.setDemandDate(formattedString);
        demand.setDemandVersion(maxVersion + 1);
        demand.setActiveVersion(true);

        return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
    }

    @Modifying
    @Transactional
    @PutMapping("/updateclassification/{demandCode}")
    public ResponseEntity<Object> updateClassification(@PathVariable(value = "demandCode") Integer
                                                               demandCode, @RequestBody DemandDTO demandDTO) {
        List<Demand> demandList = demandService.findByDemandCode(demandCode);

        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                demand.setClassification(demandDTO.getClassification());
                return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("OUT");
    }

    @Modifying
    @Transactional
    @PutMapping("/updatestatus/{demandCode}")
    public ResponseEntity<Object> updateStatus(@PathVariable(value = "demandCode") Integer
                                                       demandCode, @RequestBody DemandDTO demandDTO) {
        List<Demand> demandList = demandService.findByDemandCode(demandCode);

        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                demand.setDemandStatus(demandDTO.getDemandStatus());
                return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("OUT");
    }

    @GetMapping("/filter/{type}/{value}")
    public ResponseEntity<Object> filter(@PathVariable(value = "type") String
                                                 type, @PathVariable(value = "value") String value) throws IOException {
        if (type.equals("status")) {
            List<Demand> demands = findByStatus(value);
            saveExcel("demands(" + demands.size() + ").xlsx", demands);
            return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findByDemandStatus(value));
        }

        return ResponseEntity.status(HttpStatus.FOUND).body("No demands found");
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Demand>> findAll
            (@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());

        List<Demand> demandList = demandService.findAll();
        for (Demand demand : demandList) {
            if (demand.getScore() != null) {
                demand.setScore(score(demand));
                demandRepository.saveAndFlush(demand);
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findAllByActiveVersionOrderByScoreDesc(pageable));
    }

    @Modifying
    @Transactional
    @PutMapping("/approve/{demandCode}/{approver}")
    public ResponseEntity<Object> approve(@PathVariable(value = "demandCode") Integer demandCode, @PathVariable(value = "approver") Worker approver) {
        List<Demand> demandList = demandService.findAllByDemandCode(demandCode);

        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                demand.setScore(score(demand));
                demand.setApprover(approver);
                return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("OUT");
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

    @Modifying
    @Transactional
    @PutMapping("/costcenter/{demandCode}")
    public ResponseEntity<Object> updateCostCenter(@PathVariable(value = "demandCode") Integer
                                                           demandCode, @RequestBody DemandDTO demandDTO) {
        List<Demand> demandList = demandService.findByDemandCode(demandCode);

        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                demand.setCostCenter(demandDTO.getCostCenter());
                return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("OUT");
    }

    @GetMapping("/demand/{demandCode}/{demandVersion}")
    public ResponseEntity<Object> findByDemandCodeAndDemandVersion(@PathVariable(value = "demandCode") Integer
                                                                           demandCode, @PathVariable(value = "demandVersion") Integer demandVersion) {
        return ResponseEntity.status(HttpStatus.OK).body(demandService.findByDemandCodeAndDemandVersion(demandCode, demandVersion));
    }

    @Modifying
    @Transactional
    @PutMapping("/setactive/{demandCode}/{nextDemandVersion}")
    public ResponseEntity<Object> setActiveVersion(@PathVariable(value = "demandCode") Integer
                                                           demandCode, @PathVariable(value = "nextDemandVersion") Integer nextDemandVersion) {
        List<Demand> demandList = demandService.findByDemandCode(demandCode);
        Demand returnDemand = new Demand();

        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                demand.setActiveVersion(false);
                demandRepository.saveAndFlush(demand);
            }
            if (demand.getDemandVersion() == nextDemandVersion) {
                demand.setActiveVersion(true);
                demandRepository.saveAndFlush(demand);
                returnDemand = demand;
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(returnDemand);
    }

    @GetMapping("/approver/{workerCode}")
    public ResponseEntity<Object> findByApproverCode(@PathVariable(value = "workerCode") Worker workerCode) {
        return ResponseEntity.status(HttpStatus.OK).body(demandService.findAllByApprover(workerCode));
    }

    @GetMapping("/analyst/{workerCode}")
    public ResponseEntity<Object> findByAnalystCode(@PathVariable(value = "workerCode") Worker workerCode) {
        return ResponseEntity.status(HttpStatus.OK).body(demandService.findAllByClassificationByAnalystRegistration(workerCode));
    }

    @GetMapping("/requester/{workerCode}")
    public ResponseEntity<Object> findByWorkerCode(@PathVariable(value = "workerCode") Worker workerCode) {
        return ResponseEntity.status(HttpStatus.OK).body(demandService.findAllByRequesterRegistration(workerCode));
    }
}
