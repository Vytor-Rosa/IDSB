package net.weg.gedesti.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

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

    @GetMapping("/order/{name}/{type}")
    public ResponseEntity<List<Demand>> order(@PathVariable(value = "name") String name, @PathVariable(value = "type") String type) {
        List<Demand> demands = demandService.findAll();
        if(name.equals("score")){
            if(type.equals("up")) {
                demands.sort(Comparator.comparing(Demand::getScore).reversed());
            }else{
                demands.sort(Comparator.comparing(Demand::getScore));
            }
        }else if(name.equals("dates")){
            if(type.equals("up")) {
                demands.sort(Comparator.comparing(Demand::getDemandDate).reversed());
            }else{
                demands.sort(Comparator.comparing(Demand::getDemandDate));
            }
        }else if(name.equals("code")){
            if(type.equals("up")) {
                demands.sort(Comparator.comparing(Demand::getDemandCode).reversed());
            }else{
                demands.sort(Comparator.comparing(Demand::getDemandCode));
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demands);
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
    public ResponseEntity<Object> savePdf(@PathVariable(value = "demandCode") Integer demandCode, HttpServletResponse response) throws IOException, DocumentException {
        try {
            List<Demand> demandOptional = demandService.findByDemandCode(demandCode);
            Demand demand = new Demand();

            for (Demand demands : demandOptional) {
                if (demands.getActiveVersion() == true) {
                    demand = demands;
                }
            }

            Document document = new Document();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            String path = new File(".").getCanonicalPath();
            Image logo = Image.getInstance(path + "\\src\\main\\java\\net\\weg\\gedesti\\controller\\filePdf\\img.png");
            logo.setAlignment(Element.ALIGN_RIGHT);
            logo.scaleToFit(50, 50);
            document.add(logo);

            String hexColor = "#00579D";
            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
            BaseColor baseColor = new BaseColor(red, green, blue);

            Paragraph title = new Paragraph(new Phrase(20F, demand.getDemandTitle() + " - " + demand.getDemandCode(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, baseColor)));
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
            if (!demand.getDemandStatus().equals("Backlog")) {
                Paragraph analist = new Paragraph();
                boldChunk = new Chunk("Analista Responsavel: ");
                boldChunk.setFont(fontBold);
                normalChunk = new Chunk(demand.getClassification().getAnalistRegistry().getWorkerName());
                normalChunk.setFont(fontNormal);
                analist.add(boldChunk);
                analist.add(normalChunk);
                document.add(analist);
            }
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

            Paragraph paragraph = new Paragraph();
            paragraph.add(new Phrase(20F, "Obrigação legal: ", FontFactory.getFont(FontFactory.HELVETICA, 10)));
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
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableCostCenter.addCell(headerCell);
            headerCell.setPhrase(new Phrase("Nome do Centro de Custo", fontBold));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableCostCenter.addCell(headerCell);

            // Adicionar linhas da tabela
            for (CostCenter costCenter : demand.getCostCenter()) {
                PdfPCell columnCell = new PdfPCell();
                columnCell.setPhrase(new Phrase(costCenter.getCostCenterCode() + "", fontNormal));
                columnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableCostCenter.addCell(columnCell);
                columnCell.setPhrase(new Phrase(costCenter.getCostCenter(), fontNormal));
                columnCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableCostCenter.addCell(columnCell);
            }

            document.add(tableCostCenter);
            document.add(quebra);

            // Classificação
            if (!demand.getDemandStatus().equals("Backlog")) {
                // Tamanho
                Paragraph size = new Paragraph();
                boldChunk = new Chunk("Tamanho: ");
                boldChunk.setFont(fontBold);
                normalChunk = new Chunk(demand.getClassification().getClassificationSize());
                normalChunk.setFont(fontNormal);
                size.add(boldChunk);
                size.add(normalChunk);
                document.add(size);

                // Sessão de TI responsável
                Paragraph itSection = new Paragraph();
                boldChunk = new Chunk("Sessão de TI Responsável: ");
                boldChunk.setFont(fontBold);
                normalChunk = new Chunk(demand.getClassification().getItSection() + "");
                normalChunk.setFont(fontNormal);
                itSection.add(boldChunk);
                itSection.add(normalChunk);
                document.add(itSection);

                // Bu solicitante
                Paragraph requesterBu = new Paragraph();
                boldChunk = new Chunk("BU solicitante: ");
                boldChunk.setFont(fontBold);
                normalChunk = new Chunk(demand.getClassification().getRequesterBu().getBu() + "");
                normalChunk.setFont(fontNormal);
                requesterBu.add(boldChunk);
                requesterBu.add(normalChunk);
                document.add(requesterBu);

                ///BUs beneficiadas
                Paragraph beneficiariesBUsTitle = new Paragraph(new Phrase(20F, "BUs Beneficiadas:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
                document.add(beneficiariesBUsTitle);
                List<Bu> beneficiaryBuList = demand.getClassification().getBeneficiaryBu();

                for (Bu bu : beneficiaryBuList) {
                    Paragraph beneficiariesBUsAdd = new Paragraph(new Phrase(20F, "• " + bu.getBu(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                    document.add(beneficiariesBUsAdd);
                }
                document.add(quebra);

                if (demand.getDemandStatus().equals("BacklogComplement")) {
                    //Codigo PPM
                    Phrase ppmCodeTitle = new Phrase(20F, "Código PPM:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
                    Phrase ppmCodeAdd = new Phrase(20F, demand.getClassification().getPpmCode(), FontFactory.getFont(FontFactory.HELVETICA, 10));
                    Phrase combined = new Phrase();
                    combined.add(ppmCodeTitle);
                    combined.add(ppmCodeAdd);
                    document.add(combined);
                    document.add(quebra);

                    //Link Epic Jira
                    Phrase linkJiraTitle = new Phrase(20F, "Link Epic Jira:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
                    Phrase linkJiraAdd = new Phrase(20F, demand.getClassification().getEpicJiraLink(), FontFactory.getFont(FontFactory.HELVETICA, 10));
                    combined = new Phrase();
                    combined.add(linkJiraTitle);
                    combined.add(linkJiraAdd);
                    document.add(combined);
                    document.add(quebra);
                }
            }
            document.close();

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "attachment; filename=" + demand.getDemandTitle() + " - " + demand.getDemandCode() + ".pdf")
                    .body(resource);
        } catch (
                Exception e) {
            throw new IOException();
        }
    }


    private static class ImageIOHelper {
        static byte[] writeImageToByteArray(BufferedImage image, String format) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }

    }

    @PostMapping("/filter")
    public byte[] saveExcel(@RequestBody List<Demand> demands) throws IOException {
        try (var workbook = new XSSFWorkbook();
             var outputStream = new ByteArrayOutputStream()) {
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
                    cell.setCellValue("N/A");
                    cell = row.createCell(10);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("N/A");
                    cell = row.createCell(11);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("N/A");
                    cell = row.createCell(12);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("N/A");
                    cell = row.createCell(13);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue("N/A");
                }
                index++;
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void openFile(String attachmentName) throws IOException {
        try {
            File file = new File(attachmentName);
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
                                               demandJson, @RequestParam(value = "demandAttachments", required = false) List<MultipartFile> demandAttachment) throws ParseException {
        DemandUtil demandUtil = new DemandUtil();
        Demand demand = demandUtil.convertJsonToModel(demandJson);
        demand.setDemandVersion(1);
        demand.setDemandHour(LocalTime.now());

        LocalDate createDate = LocalDate.now();
        Date date = Date.from(createDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        demand.setDemandDate(date);

        List<Demand> demands = demandRepository.findAllByActiveVersion();
        Integer size = demands.size();
        demand.setDemandCode(size + 1);
        demand.setActiveVersion(true);

        if (demandAttachment != null) {
            demand.setDemandAttachment(demandAttachment);
        }

        demand.setScore(0.0);

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
            IOException, ParseException {
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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(Date.from(createDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Date date = dateFormat.parse(strDate);
        demand.setDemandDate(date);
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

//    @PostMapping("/filter")
//    public ResponseEntity<Object> filter(@RequestBody List<Demand> demands) throws IOException {
//        if (!demands.isEmpty()) {
//            saveExcel("Demands.xls", demands);
//            return ResponseEntity.status(HttpStatus.FOUND).body(demands);
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No demands found!");
//    }

    @GetMapping("/page")
    public ResponseEntity<Page<Demand>> findAll
            (@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());

        List<Demand> demandList = demandService.findAll();
        for (Demand demand : demandList) {
            if (demand.getScore() != 0.0) {
                demand.setScore(score(demand));
                demandRepository.saveAndFlush(demand);
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findAllByActiveVersionOrderByScoreDesc(pageable));
    }

    @GetMapping("/manager/page")
    public ResponseEntity<Page<Demand>> findAllManager
            (@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());

        List<Demand> demandList = demandService.findAll();
        for (Demand demand : demandList) {
            if (demand.getScore() != 0.0) {
                demand.setScore(score(demand));
                demandRepository.saveAndFlush(demand);
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findAllByActiveVersionAndDemandStatusOrderByScoreDesc(pageable));
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
        Date createDate = demandDate.getDemandDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
        String formatDate = dateFormat.format(createDate);
        long days = ChronoUnit.DAYS.between(LocalDate.parse(formatDate, formatter), actualDate);

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
        List<Demand> demandList = demandService.findAllByApprover(workerCode);
        List<Demand> activeDemands = new ArrayList<>();
        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                activeDemands.add(demand);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(activeDemands);
    }

    @GetMapping("/analyst/{workerCode}")
    public ResponseEntity<Object> findByAnalystCode(@PathVariable(value = "workerCode") Worker workerCode) {
        List<Demand> demandList = demandService.findAllByClassificationByAnalystRegistration(workerCode);
        List<Demand> activeDemands = new ArrayList<>();
        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                activeDemands.add(demand);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(activeDemands);
    }

    @GetMapping("/requester/{workerCode}")
    public ResponseEntity<Object> findByWorkerCode(@PathVariable(value = "workerCode") Worker workerCode) {
        List<Demand> demandList = demandService.findAllByRequesterRegistration(workerCode);
        List<Demand> activeDemands = new ArrayList<>();
        for (Demand demand : demandList) {
            if (demand.getActiveVersion() == true) {
                activeDemands.add(demand);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(activeDemands);
    }

    @GetMapping("/version")
    public ResponseEntity<Object> findAllByDemandVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(demandService.findAllByVersion(1));
    }
}
