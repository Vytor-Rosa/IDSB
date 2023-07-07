package net.weg.gedesti.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.AgendaDTO;
import net.weg.gedesti.model.entity.Agenda;
import net.weg.gedesti.model.entity.Demand;
import net.weg.gedesti.model.entity.Minute;
import net.weg.gedesti.model.entity.Proposal;
import net.weg.gedesti.model.service.AgendaService;
import net.weg.gedesti.repository.AgendaRepository;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agenda")
public class AgendaController {

    private AgendaService agendaService;
    private AgendaRepository agendaRepository;

    @GetMapping
    public ResponseEntity<List<Agenda>> findAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(agendaService.findAll());
    }

    @GetMapping("/order/{name}/{type}")
    public ResponseEntity<List<Agenda>> order(@PathVariable(value = "name") String name, @PathVariable(value = "type") String type) {
        List<Agenda> agendas = agendaService.findAll();
        if(name.equals("dates")){
            if(type.equals("up")) {
                agendas.sort(Comparator.comparing(Agenda::getAgendaDate).reversed());
            }else{
                agendas.sort(Comparator.comparing(Agenda::getAgendaDate));
            }
        }else if(name.equals("code")){
            if(type.equals("up")) {
                agendas.sort(Comparator.comparing(Agenda::getAgendaCode).reversed());
            }else{
                agendas.sort(Comparator.comparing(Agenda::getAgendaCode));
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(agendas);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Agenda>> findAll(@PageableDefault(page = 0, value = 1, size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        pageable = PageRequest.of(pageNumber > 0 ? pageNumber - 1 : 0, pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.status(HttpStatus.FOUND).body(agendaService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AgendaDTO agendaDTO) {
        Agenda agenda = new Agenda();
        BeanUtils.copyProperties(agendaDTO, agenda);

        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.save(agenda));
    }

    @GetMapping("/{agendaCode}")
    public ResponseEntity<Object> findById(@PathVariable(value = "agendaCode") Integer agendaCode) {
        Optional<Agenda> optionalAgenda = agendaService.findById(agendaCode);

        if (optionalAgenda.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No agenda with code: " + agendaCode);
        }

        List<Agenda> listAgenda = new ArrayList<>();
        listAgenda.add(optionalAgenda.get());
        return ResponseEntity.status(HttpStatus.FOUND).body(listAgenda);
    }

    @DeleteMapping("/{agendaCode}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "agendaCode") Integer agendaCode) {
        Optional<Agenda> agendaOptional = agendaService.findById(agendaCode);
        if (agendaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No agenda with code: " + agendaCode);
        }
        agendaService.deleteById(agendaCode);
        return ResponseEntity.status(HttpStatus.FOUND).body("Agenda " + agendaCode + " successfully deleted!");
    }

    @GetMapping("/proposal/{proposalCode}")
    public ResponseEntity<Object> findByProposalCode(@PathVariable(value = "proposalCode") Proposal proposalCode) {
        List<Agenda> listAgenda = agendaService.findAll();
        for (Agenda agenda : listAgenda) {
            if (agenda.getProposals().contains(proposalCode)) {
                return ResponseEntity.status(HttpStatus.FOUND).body(agenda);
            }
        }
        return ResponseEntity.status(HttpStatus.FOUND).body("Error! No agenda with proposal code: " + proposalCode);
    }

    @Modifying
    @Transactional
    @PutMapping("/{agendaCode}")
    public ResponseEntity<Object> update(@PathVariable(value = "agendaCode") Integer agendaCode, @RequestBody @Valid AgendaDTO agendaDTO) {
        Optional<Agenda> optionalAgenda = agendaService.findById(agendaCode);
        if (optionalAgenda.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No agenda with code: " + agendaCode);
        }
        Agenda agenda = optionalAgenda.get();
        BeanUtils.copyProperties(agendaDTO, agenda);
        agendaRepository.saveAndFlush(agenda);
        return ResponseEntity.status(HttpStatus.OK).body(agenda);
    }

    @PostMapping("/filter")
    public byte[] saveExcel(@RequestBody List<Agenda> agendas) throws IOException {
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
            cell.setCellValue("Comissão");
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellValue("Data inicial");
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("Data final");
            cell.setCellStyle(style);
            cell = row.createCell(5);
            cell.setCellValue("Analista");
            cell.setCellStyle(style);
                for (Agenda agenda : agendas) {
                row = sheet.createRow(rowNum++);
                cell = row.createCell(0);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(agenda.getAgendaCode());
                cell = row.createCell(1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(agenda.getSequentialNumber());
                cell = row.createCell(2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(agenda.getCommission().getCommissionName());
                cell = row.createCell(3);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(agenda.getInitialDate());
                cell = row.createCell(4);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(agenda.getFinalDate());
                cell = row.createCell(5);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(agenda.getAnalistRegistry().getWorkerName());
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
