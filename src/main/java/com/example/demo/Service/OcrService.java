package com.example.demo.Service;

import com.example.demo.Entity.DataOrc;
import com.example.demo.Entity.OcrResult;
import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


@Service
public class OcrService implements OcrServiceImpl {
    @Autowired
    Tesseract tesseract;

    @Override
    public DataOrc result(MultipartFile file) throws IOException, TesseractException, DocumentException, NotFoundException, FormatException, ChecksumException {
        File convFile = convert(file);
        PDDocument document = PDDocument.load(convFile);
        DataOrc dataOrc = new DataOrc();
        try {
            String codeQr = checkQRPdf(document);
            System.out.println("BarCode: " + codeQr);
            switch (codeQr) {
                case "TTQT_TTTM_0001":
                    String ttqt01 = extractTextFromScannedDocument(document, tesseract);
                    String[] ttq = ttqt01.split("\\s");
                    dataOrc.setFormOfCredit(checkBox(ttq));
                    dataOrc.setResult(ttqt01);
                    dataOrc.setBarCode(codeQr);
                    break;
                case "TTQT_TTTM_0002":
                    String txt = extractTextFromScannedDocument(document, tesseract);
                    String[] data = txt.split("\\s");
                    dataOrc.setAmount(data[20]);
                    dataOrc.setLetterCredit(data[6]);
                    dataOrc.setIssueDate(data[13]);
                    dataOrc.setBeneficiary(concat(data));
                    dataOrc.setResult(txt);
                    dataOrc.setBarCode(codeQr);
                    break;
                case "TTQT_CTQT_0001":
                    String re = extractTextFromScannedDocument(document, tesseract);
                    String[] s = re.split("\\s");
                    String address = address(s);
                    String cmnd = cmnd(s);
                    dataOrc.setAddress(address);
                    dataOrc.setCmnd(cmnd);
                    dataOrc.setResult(re);
                    dataOrc.setBarCode(codeQr);
                    break;
                default:
                    String str = extractTextFromScannedDocument(document, tesseract);
                    dataOrc.setResult(str);
            }
        } finally {
            document.close();
        }
        return dataOrc;
    }

    public static String extractTextFromScannedDocument(PDDocument document, Tesseract tesseract) throws IOException, TesseractException, NotFoundException, FormatException, ChecksumException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
//            BufferedImage crop = crop(document, bufferedImage, pdfRenderer, page);
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            Rectangle rectangle = getRectangle(document);
            ImageIO.write(bufferedImage, "png", tempFile);
            String result = tesseract.doOCR(tempFile, rectangle);
            out.append(result);
        }
        return out.toString();
    }


    public static Rectangle getRectangle(PDDocument document) throws TesseractException, FormatException, ChecksumException, NotFoundException, IOException {
        Rectangle rectangle = null;
        String code = checkQRPdf(document);
        switch (code) {
            case "TTQT_TTTM_0001":
                rectangle = new Rectangle(100, 800, 2000, 300);
                break;
            case "TTQT_TTTM_0002":
                rectangle = new Rectangle(300, 800, 1800, 400);
                break;
            case "TTQT_CTQT_0001":
                rectangle = new Rectangle(130, 800, 1801, 300);
                break;
        }
        return rectangle;
    }

    public static String checkQRPdf(PDDocument document) throws IOException, TesseractException, NotFoundException, FormatException, ChecksumException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            BufferedImage crop = cropImage(bufferedImage, 1860, 50, 550, 300);
            File file = new File(String.format("ToImage-img-%d.png", page));
            ImageIO.write(crop, "png", file);
            String result = readBarcode(file);
            out.append(result);
            if (result != null) {
                return out.toString();
            }
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

    public static String readBarcode(File file) throws IOException, FormatException, ChecksumException, NotFoundException {
        try {
            InputStream barCodeInputStream = new FileInputStream(file);
            BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);
            BinaryBitmap bitmap = null;
            LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
            bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result re = reader.decode(bitmap);
            if (re.getText() != null) {
                return re.getText();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return "";
    }

    public static String checkBox(String[] data) {
        StringBuilder str = new StringBuilder();
        str.append(data[21]);
        str.append(data[22]);
        str.append(" ");
        str.append(data[23]);
        str.append(data[24]);
        str.append(" ");
        str.append(data[25]);
        return str.toString().replace("IOther", "Other");
    }

    public static String cmnd(String[] data) {
        StringBuilder str = new StringBuilder();
        str.append(data[12]);
        str.append(" ");
        str.append(data[13]);
        str.append(" ");
        str.append(data[14]);
        str.append(" ");
        str.append(data[15]);
        str.append(" ");
        str.append(data[16]);
        return str.toString();
    }

    public static String address(String[] data) {
        StringBuilder str = new StringBuilder();
        for (int i = 34; i <= 44; i++) {
            str.append(data[i]);
            str.append(" ");
        }
        return str.substring(0, str.length() - 1);
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

    public static String readPdf(PDDocument document, Tesseract tesseract) throws IOException, TesseractException, NotFoundException, FormatException, ChecksumException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bufferedImage, "png", tempFile);
            Rectangle rectangle = new Rectangle();
            rectangle.setRect(50, 100, 1800, 400);
            String result = tesseract.doOCR(tempFile, rectangle);
            out.append(result);
        }
        return out.toString();
    }

    public static BufferedImage crop(PDDocument document, BufferedImage bufferedImage, PDFRenderer pdfRenderer, int page) throws TesseractException, FormatException, ChecksumException, NotFoundException, IOException {
        String code = checkQRPdf(document);
        BufferedImage image = null;
        switch (code) {
            case "TTQT_TTTM_0001":
                image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                break;
            case "TTQT_TTTM_0002":
                image = cropImage(bufferedImage, 300, 800, 1800, 400);
                break;
            case "TTQT_CTQT_0001":
                image = cropImage(bufferedImage, 20, 100, 1801, 400);
                break;
        }
        return image;
    }
}
