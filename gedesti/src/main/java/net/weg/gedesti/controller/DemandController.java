package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.DemandDTO;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.service.ClassificationService;
import net.weg.gedesti.model.service.DemandService;
import net.weg.gedesti.model.service.WorkerService;
import net.weg.gedesti.repository.DemandRepository;
import net.weg.gedesti.util.DemandUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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
            System.out.println("demand: " + demand.getDemandStatus().toLowerCase());
            System.out.println("value: " + value.toLowerCase());
            if (demand.getDemandStatus().trim().toLowerCase().contains(value.trim().toLowerCase())) {
                demandsStatus.add(demand);
            }
        }
        return demandsStatus;
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Demand>> findAll(@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findAll(pageable));
    }

    @PostMapping("/excel")
    public void saveExcel(final String attachmentName, final List<Demand> demands) throws IOException {
        try(var workbook = new XSSFWorkbook();
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
                for(Demand demand : demands) {
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
                    if(demand.getClassification() != null) {
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
                    }else{
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

            System.out.println("Excel file created successfully");
            workbook.write(outputStream);
            outputStream.close();
            openFile(attachmentName);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openFile(String attachmentName) throws IOException {
        try {
            File file = new File("C:\\Users\\" + System.getProperty("user.name") +"\\Documents\\GitHub\\IDSB\\gedesti\\" + attachmentName);
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", "start", "", file.toString());
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestParam(value = "demand") @Valid String demandJson, @RequestParam(value = "demandAttachment", required = false) MultipartFile demandAttachment) {
        DemandUtil demandUtil = new DemandUtil();
        Demand demand = demandUtil.convertJsonToModel(demandJson);
        if (demandAttachment != null) {
            demand.setDemandAttachment(demandAttachment);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(demandService.save(demand));
    }

    @GetMapping("/{demandCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "demandCode") Integer demandCode) {
        Optional<Demand> demandOptional = demandService.findById(demandCode);
        if (demandOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No demand with code: " + demandCode);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(demandOptional);
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
                                         @RequestParam(value = "demandAttachment", required = false) MultipartFile demandAttachment  ) {
        if (!demandService.existsById(demandCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        DemandUtil demandUtil = new DemandUtil();
        Demand demand = demandUtil.convertJsonToModel(demandJson);
        if(demandAttachment != null) {
            demand.setDemandAttachment(demandAttachment);
        }
        demand.setDemandCode(demandCode);


        return ResponseEntity.status(HttpStatus.CREATED).body(demandService.saveAndFlush(demand));
    }

    @Modifying
    @Transactional
    @PutMapping("/updateclassification/{demandCode}")
    public ResponseEntity<Object> updateClassification(@PathVariable(value = "demandCode") Integer demandCode, @RequestBody DemandDTO demandDTO) {
        if (!demandService.existsById(demandCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Demand demand = demandRepository.findById(demandCode).get();
        demand.setClassification(demandDTO.getClassification());
        return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
    }

    @Modifying
    @Transactional
    @PutMapping("/updatestatus/{demandCode}")
    public ResponseEntity<Object> updateStatus(@PathVariable(value = "demandCode") Integer demandCode, @RequestBody DemandDTO demandDTO) {
        if (!demandService.existsById(demandCode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("doesn't exist");
        }

        Demand demand = demandRepository.findById(demandCode).get();
        demand.setDemandStatus(demandDTO.getDemandStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(demandRepository.saveAndFlush(demand));
    }

    @GetMapping("/filter/{type}/{value}")
    public ResponseEntity<Object> filter(@PathVariable(value = "type") String type, @PathVariable(value = "value") String value) throws IOException {
        if (type.equals("status")) {
            List<Demand> demands = findByStatus(value);
            saveExcel("demands(" + demands.size() + ").xlsx", demands);
            return ResponseEntity.status(HttpStatus.FOUND).body(demandService.findByDemandStatus(value));
        }

        return ResponseEntity.status(HttpStatus.FOUND).body("No demands found");
    }
}
