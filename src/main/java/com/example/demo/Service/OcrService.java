package com.example.demo.Service;

import com.example.demo.Entity.DataOrc;
import com.example.demo.Entity.OcrResult;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
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
import java.util.HashMap;
import java.util.Map;


@Service
public class OcrService implements OcrServiceImpl {
    @Autowired
    Tesseract tesseract;

    @Override
    public DataOrc result(MultipartFile file) throws IOException, TesseractException, DocumentException, NotFoundException {
        File convFile = convert(file);
        PDDocument document = PDDocument.load(convFile);
        String codeQr = checkQRPdf(document, tesseract);
        String[] qr = codeQr.split("\\s");
        System.out.println(qr[0]);
        DataOrc dataOrc = new DataOrc();
        switch (qr[0]) {
            case "MB.LCNK—02":
                System.out.println("mai lam");
                break;
            case "MB":
                String txt = extractTextFromScannedDocument(document, tesseract);
                String[] data = txt.split("\\s");
                dataOrc.setAmount(data[20]);
                dataOrc.setLetterCredit(data[6]);
                dataOrc.setIssueDate(data[13]);
                dataOrc.setBeneficiary(concat(data));
                break;
        }
        return dataOrc;
    }

    public static String extractTextFromScannedDocument(PDDocument document, Tesseract tesseract) throws IOException, TesseractException, NotFoundException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            BufferedImage crop = cropImage(bufferedImage, 300, 800, 1800, 400);
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(crop, "png", tempFile);
            String result = tesseract.doOCR(tempFile);
            out.append(result);
        }
        return out.toString();

    }

    public static String checkQRPdf(PDDocument document, Tesseract tesseract) throws IOException, TesseractException, NotFoundException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            BufferedImage crop = cropImage(bufferedImage, 1950, 150, 450, 300);
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(crop, "png", tempFile);
            String result = tesseract.doOCR(tempFile);
            out.append(result);
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

    public static String check(MultipartFile file) throws IOException, NotFoundException {
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        File fileConvert = convert(file);
        String path = fileConvert.getPath();
        System.out.println("path " + path);
        return readQR(fileConvert.getPath(), "UTF-8", hintMap);
    }

    public static Map<String, String> map() {
        Map<String, String> map = new HashMap<>();
        map.put("MB.LCNK—02", "MB.LCNK—02");
        map.put("MB.LCNK—04", "MB.LCNK—04");
        return map;
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


    public static String readQR(String path, String charset,
                                Map hashMap)
            throws FileNotFoundException, IOException,
            NotFoundException {
        BinaryBitmap binaryBitmap
                = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(
                                new FileInputStream(path)))));

        Result result
                = new MultiFormatReader().decode(binaryBitmap);

        return result.getText();
    }

}
