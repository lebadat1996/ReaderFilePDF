package com.example.demo.Service;

import com.example.demo.Entity.DataOrc;
import com.example.demo.Entity.OcrResult;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.ghost4j.document.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


@Service
public class OcrService implements OcrServiceImpl {
    @Autowired
    Tesseract tesseract;

    @Override
    public DataOrc result(MultipartFile file) throws IOException, TesseractException, DocumentException {
        File convFile = convert(file);
        PDDocument document = PDDocument.load(convFile);
        String txt = extractTextFromScannedDocument(document, tesseract);
        String[] data = txt.split("\\s");
        System.out.println(txt);
        DataOrc dataOrc = new DataOrc();
        dataOrc.setAmount(data[20]);
        dataOrc.setLetterCredit(data[6]);
        dataOrc.setIssueDate(data[13]);
        dataOrc.setBeneficiary(concat(data));
        return dataOrc;
    }

    public static String extractTextFromScannedDocument(PDDocument document, Tesseract tesseract) throws IOException, TesseractException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.ARGB);
            BufferedImage crop = cropImage(bufferedImage, 300, 800, 1800, 400);
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(crop, "png", tempFile);
            String result = tesseract.doOCR(tempFile);
            out.append(result);
            tempFile.delete();
        }
        return out.toString();

    }

    public static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);
        return croppedImage;
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        if (convFile.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public static String concat(String[] data) {
        StringBuilder str = new StringBuilder();
        str.append(data[25]);
        str.append(" ");
        str.append(data[26]);
        str.append(" ");
        str.append(data[27]);
        str.append(" ");
        str.append(data[28]);
        str.append(" ");
        str.append(data[29]);
        str.append(" ");
        str.append(data[30]);
        str.append(" ");
        str.append(data[31]);
        return str.toString();
    }

}
