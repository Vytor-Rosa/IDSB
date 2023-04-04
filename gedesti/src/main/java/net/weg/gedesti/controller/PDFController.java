package net.weg.gedesti.controller;

import com.itextpdf.text.DocumentException;
import net.weg.gedesti.model.entity.Minute;
import net.weg.gedesti.model.service.MinuteService;
import net.weg.gedesti.model.service.NotificationService;
import net.weg.gedesti.util.model.MinuteModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PDFController {

    private MinuteService minuteService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@PathVariable Integer id) throws DocumentException, IOException {
//        Minute minute = minuteService.findById(id).get();
        String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Weg_logo_blue_vector.svg/1200px-Weg_logo_blue_vector.svg.png"; // Substitua pelo URL da sua imagem
        byte[] pdfBytes = MinuteModel.generate(imageUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "user.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}