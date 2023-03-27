package net.weg.gedesti.util.model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import net.weg.gedesti.model.entity.Minute;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MinuteModel {
    public static final Font NORMAL = new Font(Font.FontFamily.HELVETICA, 10);

    public static byte[] generate(String imageUrl) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);

        document.open();

        Image image = Image.getInstance(new URL(imageUrl));
        image.scaleToFit(image.getWidth() * 0.04f, image.getHeight() * 0.04f);
        image.setAlignment(Image.RIGHT | Image.TEXTWRAP);
        document.add(image);

        Paragraph title = new Paragraph("ATA REUNIÃO COMISSÃO PROCESSOS DE VENDAS E DESENVOLVIMENTO DE PRODUTO", NORMAL);
        document.add(title);


        document.close();
        return out.toByteArray();
    }
}