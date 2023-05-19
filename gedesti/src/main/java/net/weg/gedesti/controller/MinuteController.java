package net.weg.gedesti.controller;

import lombok.AllArgsConstructor;
import net.weg.gedesti.dto.MinuteDTO;
import net.weg.gedesti.model.entity.Agenda;
import net.weg.gedesti.model.entity.Minute;
import net.weg.gedesti.model.service.MinuteService;
import net.weg.gedesti.util.MinuteUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/minutes")
public class MinuteController {

    private MinuteService minuteService;

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

    public void savePdf(final Minute minute) {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
            contentStream.beginText();

            contentStream.newLineAtOffset(0, -20);
            contentStream.showText(minute.getMinuteName() + " - " + minute.getMinuteCode());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Data de início: " + minute.getMinuteStartDate());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Data de término: " + minute.getMinuteEndDate());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Tipo de ata: " + minute.getMinuteType());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Diretor: " + minute.getDirector());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Pauta: " + minute.getAgenda().getAgendaCode());
            contentStream.newLineAtOffset(0, -20);

            // Adicionar dados resumidos de todas as propostas

            contentStream.endText();
            contentStream.close();

            document.save(new File("C:\\Users\\" + System.getProperty("user.name") + "\\Downloads\\" + minute.getMinuteCode() + " - " + minute.getMinuteName() + ".pdf"));
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
