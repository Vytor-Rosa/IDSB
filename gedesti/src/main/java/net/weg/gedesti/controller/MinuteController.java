package net.weg.gedesti.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.MinuteDTO;
import net.weg.gedesti.model.entity.*;
import net.weg.gedesti.model.service.ExpensesService;
import net.weg.gedesti.model.service.MinuteService;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    public ResponseEntity<InputStreamResource> savePdf(@PathVariable(value = "minuteCode") Integer minuteCode, HttpServletResponse response) throws IOException {
        Optional<Minute> minuteOptional = minuteService.findById(minuteCode);
        Minute minute = minuteOptional.get();

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            String path = new File(".").getCanonicalPath();
            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(path + "\\src\\main\\java\\net\\weg\\gedesti\\controller\\filePdf\\img.png");
            logo.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            logo.scaleToFit(50, 50);
            document.add(logo);

            String hexColor = "#00579D";
            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
            BaseColor baseColor = new BaseColor(red, green, blue);

            com.itextpdf.text.Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            com.itextpdf.text.Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // Título
            Paragraph title = new Paragraph(new Phrase(20F, "ATA REUNIÃO " + minute.getAgenda().getCommission().getCommissionName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, baseColor)));
            document.add(title);
            Paragraph quebra = new Paragraph();
            quebra.add(" ");
            document.add(quebra);

            // Número ATA
            Paragraph numberAta = new Paragraph(new Phrase(20F, "ATA N°" + minute.getMinuteCode(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            numberAta.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(numberAta);

            // Data
            Paragraph date = new Paragraph(new Phrase(20F, "Data: " + minute.getMinuteStartDate(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            date.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(date);

            // Início
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime dateTimeStart = LocalDateTime.parse(minute.getAgenda().getInitialDate(), formatter);
            int hourStart = dateTimeStart.getHour();
            int minuteStart = dateTimeStart.getMinute();
            Paragraph initial = new Paragraph(new Phrase(20F, "Início: " + hourStart + ":" + minuteStart + "h", FontFactory.getFont(FontFactory.HELVETICA, 8)));
            initial.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(initial);

            // Término
            LocalDateTime dateTimeEnd = LocalDateTime.parse(minute.getAgenda().getFinalDate(), formatter);
            int hourEnd = dateTimeEnd.getHour();
            int minuteEnd = dateTimeEnd.getMinute();
            Paragraph end = new Paragraph(new Phrase(20F, "Término: " + hourEnd + ":" + minuteEnd + "h", FontFactory.getFont(FontFactory.HELVETICA, 8)));
            end.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
            document.add(end);

            // Propostas
            List<Proposal> proposalList = new ArrayList<>();

            if (minute.getMinuteType().equals("Published") || minute.getMinuteType().equals("DG")) {
                for (Proposal proposal : minute.getAgenda().getProposals()) {
                    if (proposal.getProposalStatus().equals("ApprovedDG") || proposal.getProposalStatus().equals("RejectedDG")) {
                        proposalList.add(proposal);
                    }
                }
            } else {
                for (Proposal proposal : minute.getAgenda().getProposals()) {
                    if (proposal.getProposalStatus().equals("ApprovedComission") || proposal.getProposalStatus().equals("RejectedComission")) {
                        proposalList.add(proposal);
                    }
                }
            }

            for (Proposal proposal : proposalList) {
                Paragraph titleProposal = new Paragraph(new Phrase(20F, proposal.getProposalName() + " - " + proposal.getProposalCode(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, baseColor)));
                document.add(titleProposal);
                document.add(quebra);

                // Objetivo
                Paragraph objectiveTitle = new Paragraph(new Phrase(20F, "Objetivo: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
                document.add(objectiveTitle);
                String objective = proposal.getDemand().getDemandObjective();
                objective = objective.replaceAll("&nbsp;", " ");
                Document doc = Jsoup.parse(objective);
                String objectiveFinal = doc.text();
                Paragraph objectiveAdd = new Paragraph(new Phrase(20F, objectiveFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                document.add(objectiveAdd);
                document.add(quebra);

                // Escopo da proposta
                Paragraph scopeProposalTitle = new Paragraph(new Phrase(20F, "Escopo da proposta: ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
                document.add(scopeProposalTitle);
                String scopeProposal = proposal.getDescriptiveProposal();
                scopeProposal = scopeProposal.replaceAll("&nbsp;", " ");
                doc = Jsoup.parse(scopeProposal);
                String scopeProposalFinal = doc.text();
                Paragraph scopeProposalAdd = new Paragraph(new Phrase(20F, scopeProposalFinal, FontFactory.getFont(FontFactory.HELVETICA, 10)));
                document.add(scopeProposalAdd);
                document.add(quebra);

                // Custos totais
                Paragraph totalCosts = new Paragraph();
                Chunk boldChunk = new Chunk("Custos totais: ");
                boldChunk.setFont(fontBold);
                Chunk normalChunk = new Chunk("R$" + proposal.getTotalCosts() + "");
                normalChunk.setFont(fontNormal);
                totalCosts.add(boldChunk);
                totalCosts.add(normalChunk);
                document.add(totalCosts);
                document.add(quebra);

                // add tabela da proposta de custos

                // Período de execução
                Paragraph executionPeriod = new Paragraph();
                boldChunk = new Chunk("Período de execução: ");
                boldChunk.setFont(fontBold);

                String sourceFormat = "yyyy-MM-dd HH:mm:ss.SSSSSS";
                String targetFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(sourceFormat);

                Date initialDate = sdf.parse(proposal.getInitialRunPeriod().toString());
                Date finalDate = sdf.parse(proposal.getFinalExecutionPeriod().toString());

                LocalDate localDateIntial = initialDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                LocalDate localDateFinal = finalDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                DateTimeFormatter formatterExecutionPeriod = DateTimeFormatter.ofPattern(targetFormat);

                String dateFormattedInitial = localDateIntial.format(formatterExecutionPeriod);
                String dateFormattedFinal = localDateFinal.format(formatterExecutionPeriod);

                normalChunk = new Chunk(dateFormattedInitial + " à " + dateFormattedFinal);
                normalChunk.setFont(fontNormal);
                executionPeriod.add(boldChunk);
                executionPeriod.add(normalChunk);
                document.add(executionPeriod);

                // Payback
                Paragraph payback = new Paragraph();
                boldChunk = new Chunk("Payback: ");
                boldChunk.setFont(fontBold);
                normalChunk = new Chunk(proposal.getPayback() + "");
                normalChunk.setFont(fontNormal);
                payback.add(boldChunk);
                payback.add(normalChunk);
                document.add(payback);

                // Responsável pelo negócio
                Paragraph responsibleForBusiness = new Paragraph();
                boldChunk = new Chunk("Responsável pelo negócio: ");
                boldChunk.setFont(fontBold);
                normalChunk = new Chunk(proposal.getResponsibleAnalyst().getWorkerName() + "");
                normalChunk.setFont(fontNormal);
                responsibleForBusiness.add(boldChunk);
                responsibleForBusiness.add(normalChunk);
                document.add(responsibleForBusiness);
                document.add(quebra);

                // Parecer da comissão
                Paragraph commissionOpinion = new Paragraph();
                boldChunk = new Chunk("Parecer da comissão: ");
                boldChunk.setFont(fontBold);
                normalChunk = new Chunk(proposal.getCommissionOpinion() + "");
                normalChunk.setFont(fontNormal);
                commissionOpinion.add(boldChunk);
                commissionOpinion.add(normalChunk);
                document.add(commissionOpinion);
                document.add(quebra);

                // Parecer da DG
                if (proposal.getDgOpinion() != null) {
                    Paragraph dgOpinion = new Paragraph();
                    boldChunk = new Chunk("Parecer da DG: ");
                    boldChunk.setFont(fontBold);
                    normalChunk = new Chunk(proposal.getDgOpinion() + "");
                    normalChunk.setFont(fontNormal);
                    dgOpinion.add(boldChunk);
                    dgOpinion.add(normalChunk);
                    document.add(dgOpinion);
                    document.add(quebra);
                }
            }

            document.close();

            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "attachment; filename=ATA REUNIÃO" + minute.getAgenda().getCommission() + ".pdf")
                    .body(resource);
        } catch (Exception e) {
            throw new IOException();
        }

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

    @PostMapping("/filter")
    public byte[] saveExcel(@RequestBody List<Minute> minutes) throws IOException {
        try (var workbook = new XSSFWorkbook();
             var outputStream = new ByteArrayOutputStream()) {
            CellStyle style = workbook.createCellStyle();

            XSSFFont font = workbook.createFont();
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
            cell.setCellValue("Numero Sequencial");
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellValue("Código Pauta");
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue("Data inicial");
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("Data final");
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue("Tipo da Ata");
            cell.setCellStyle(style);
            cell = row.createCell(6);
            cell.setCellValue("Diretor");
            cell.setCellStyle(style);
            for (Minute minute : minutes) {
                row = sheet.createRow(rowNum++);
                cell = row.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(minute.getMinuteCode());
                cell = row.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(minute.getMinuteCode());
                cell = row.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(minute.getAgenda().getAgendaCode());
                cell = row.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(minute.getMinuteStartDate());
                cell = row.createCell(4);
                cell.setCellStyle(bodyStyle);
                if (minute.getMinuteEndDate() != null) {
                    cell.setCellValue(minute.getMinuteEndDate());
                } else {
                    cell.setCellValue("N/A");
                }
                cell = row.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(minute.getMinuteType());
                cell = row.createCell(6);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(minute.getDirector().getWorkerName());
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}
