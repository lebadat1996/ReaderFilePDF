package com.example.demo;

import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.spire.pdf.PdfDocument;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainTest {

    // Function to read the QR file
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

    public static String readQRCode(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
                hintMap);
        return qrCodeResult.getText();
    }

    // Driver code
    public static void main(String[] args)
            throws Exception {

//        // Path where the QR code is saved
//        PdfDocument pdf = new PdfDocument();
//        pdf.loadFromFile("C:/Users/Admin/Downloads/L.C_V1.2.pdf");
//
//        //save every PDF to .png image
//        BufferedImage image;
//        for (int i = 0; i < pdf.getPages().getCount(); i++) {
//            image = pdf.saveAsImage(i);
//            BufferedImage crop = cropImage(image, 600, 20, 170, 100);
//            File file = new File(String.format("ToImage-img-%d.png", i));
////            File file = File.createTempFile("ToImage-img-%d" + i, ".png");
//            ImageIO.write(crop, "png", file);
//            String charset = "UTF-8";
//
//            Map<EncodeHintType, ErrorCorrectionLevel> hashMap
//                    = new HashMap<EncodeHintType,
//                    ErrorCorrectionLevel>();
//
//            hashMap.put(EncodeHintType.ERROR_CORRECTION,
//                    ErrorCorrectionLevel.L);
//            System.out.println(file.getAbsolutePath());
//            System.out.println(
//                    "QRCode output: "
//                            + readQRCode(file.getAbsolutePath(), charset, hashMap));
//        }
//        pdf.close();
//        // Encoding charset
//
//        readBarCode
//        File file = new File("C:\\Users\\datlb\\Downloads\\1.pdf");
//        FileInputStream input = new FileInputStream(file);
//        MultipartFile multipartFile = new MockMultipartFile("file",
//                file.getName(), "text/plain", IOUtils.toByteArray(input));
//        File convert = convert(multipartFile);
//        PDDocument document = PDDocument.load(convert);
////        checkQRPdf(document);
////        convertPDFtoImage("C:\\Users\\datlb\\Downloads\\demo 2.pdf");
//        Tesseract tesseract = new Tesseract();
//        tesseract.setDatapath("./tessdata");
//        String result = extractTextFromScannedDocument(document, tesseract, null);
//        System.out.println(result);
//        readCheckBox("C:/Users/Admin/Downloads/MB01_LENH_CHUYEN_TIEN_BARCODE_v3.1.pdf");

        //Loading an existing PDF document
//        File file = new File("C:\\Users\\datlb\\Downloads\\demo 2.pdf");
//        PDDocument document = PDDocument.load(file);

        // Create a Splitter object
//        Splitter splitter = new Splitter();
//
//        splitter.setSplitAtPage(5);
////        p2 = splitter.setSplitAtPage(3);
//        //splitting the pages of a PDF document
//        List<PDDocument> Pages = splitter.split(document);
//
//        //Creating an iterator object
//        Iterator<PDDocument> iterator = Pages.listIterator();
//
//        //saving splits as individual PDF document
//        int i = 1;
//        while (iterator.hasNext()) {
//            PDDocument pd = iterator.next();
//            pd.save("C:\\Users\\datlb\\Downloads\\" + i++ + ".pdf");
//        }
//        System.out.println("Multiple PDF files are created successfully.");
//        document.close();
//    }
//

    }


    public static String extractTextFromScannedDocument(PDDocument document, Tesseract tesseract, String filePath) throws IOException, TesseractException, NotFoundException, FormatException, ChecksumException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        BufferedImage bufferedImage = null;
        String result = null;
        System.out.println(document.getNumberOfPages());
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            Rectangle rectangle = new Rectangle(100, 800, 2000, 300);
            ImageIO.write(bufferedImage, "png", tempFile);
            result = tesseract.doOCR(tempFile, rectangle);
            out.append(result);
        }
        return out.toString();
    }

    public static String GetPageContent(PdfReader pdfReader, int page) throws IOException {
        StringBuilder sb = new StringBuilder();

        // from http://www.java2s.com/Open-Source/CSharp/PDF/iTextSharp/iTextSharp/text/pdf/parser/PdfContentReaderTool.cs.htm
        RandomAccessFileOrArray f = pdfReader.getSafeFile();
        byte[] contentBytes = pdfReader.getPageContent(page, f);
        f.close();
        for (byte b : contentBytes) {
            sb.append((char) b);
        }
        return sb.toString();
    }


    public static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, int width, int height) {
        BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);
        return croppedImage;
    }

    public static String demo(BufferedImage bufferedImage, Tesseract tesseract, int page) throws IOException, TesseractException {
        BufferedImage crop = cropImage(bufferedImage, 1910, 150, 450, 300);
        File file = new File(String.format("ToImage-img-%d.png", page));
        ImageIO.write(crop, "png", file);
        return tesseract.doOCR(file);
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

    public static void convertPDFtoImage(String path) throws IOException {
        PdfDocument pdfDocument = new PdfDocument();
        pdfDocument.loadFromFile(path);
        BufferedImage image = null;
        for (int i = 0; i < pdfDocument.getPages().getCount(); i++) {
            image = pdfDocument.saveAsImage(i);
            File file = new File(String.format("ToImage-img-%d.png", i));
            ImageIO.write(image, "png", file);
        }
        pdfDocument.close();
    }

    public static String readBarcode(File file) throws IOException, FormatException, ChecksumException, NotFoundException {
        InputStream barCodeInputStream = new FileInputStream(file);
        BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);

        LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        Result re = reader.decode(bitmap);
        System.out.println("Barcode text is " + re.getText());
        return re.getText().toString();
    }

    public static void readCheckBox(String path) throws IOException {
        InputStream pdfIs = new FileInputStream(path);
        RandomAccessBufferedFileInputStream rbfi = new RandomAccessBufferedFileInputStream(pdfIs);

        PDFParser parser = new PDFParser(rbfi);
        parser.parse();
        try (COSDocument cosDoc = parser.getDocument()) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            PDDocument pdDoc = new PDDocument(cosDoc);
            String parsedText = pdfStripper.getText(pdDoc);
            //System.out.println("Full text"+parsedText);

            for (int i = 0; i < parsedText.length(); i++) {
                if ('☒' == parsedText.charAt(i)) {
                    System.out.println("Found a checked box at index " + i);
                    System.out.println("\\u" + Integer.toHexString(parsedText.charAt(i) | 0x10000).substring(1));
                } else if ('☐' == parsedText.charAt(i)) {
                    System.out.println("Found an unchecked box at index " + i);
                    System.out.println("\\u" + Integer.toHexString(parsedText.charAt(i) | 0x10000).substring(1));
                }
                //else {//skip}
            }
        }
    }
}
