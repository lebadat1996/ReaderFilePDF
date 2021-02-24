package com.example.demo;

import com.google.zxing.*;
import com.google.zxing.Reader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfMargins;
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
import org.apache.pdfbox.tools.PDFMerger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
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
//        File pdffile
//                = new File("C:\\Users\\datlb\\Downloads\\Demo 1.pdf");
//        PDDocument document = PDDocument.load(pdffile);
//        com.itextpdf.text.pdf.PdfReader pdfReader = new com.itextpdf.text.pdf.PdfReader("C:\\Users\\datlb\\Downloads\\Demo 1.pdf");
//        int pages = pdfReader.getNumberOfPages();
//        String pdfText = "";
//        for (int ctr = 1; ctr < pages + 1; ctr++) {
//            pdfText += PdfTextExtractor.getTextFromPage(pdfReader, ctr); // Page number cannot be 0 or will throw NPE
//            if (pdfText.equals("")) {
//                System.out.println("page: " + ctr);
//            }
//        }
//        pdfReader.close();
//        return pdfText;
//        Splitter splitter = new Splitter();
//        List<PDDocument> pdDocuments = splitter.split(document);
//        Iterator<PDDocument> iterator = pdDocuments.listIterator();
//        int i = 1;
//        while (iterator.hasNext()) {
//            PDDocument pd = iterator.next();
//            pd.save("E:\\DemoPDF\\ReaderFilePDF\\" + i++ + ".pdf");
//        }
//        System.out.println("Multiple PDF files are created successfully.");
        split();
    }

    public static void splitPDF() throws IOException {
        File pdffile
                = new File("E:\\DemoPDF\\ReaderFilePDF\\Demo 1.pdf");
        String FILE1 = "/uploads/first.pdf";
        String OUTPUT_FOLDER = "/myfiles/";
        PDDocument document = PDDocument.load(pdffile);
        Splitter splitter = new Splitter();
        splitter.setSplitAtPage(4);
        List<PDDocument> pdDocuments = splitter.split(document);
        Iterator<PDDocument> iterator = pdDocuments.listIterator();
        int i = 1;
        while (iterator.hasNext()) {
            PDDocument pd = iterator.next();
            pd.save("E:\\DemoPDF\\ReaderFilePDF\\" + i++ + ".pdf");

        }
        System.out.println("Multiple PDF files are created successfully.");
    }

    public static void split() {
        //Load the PDF file
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile("E:\\DemoPDF\\ReaderFilePDF\\Demo 1.pdf");

        //Create a new PDF file
        PdfDocument newDoc1 = new PdfDocument();

        PdfPageBase page;

        //Add 2 pages to the new PDF, and draw the content of page 1-2 of the original PDF to the newly added pages
        for (int i = 0; i < 4; i++) {
            page = newDoc1.getPages().add(doc.getPages().get(i).getSize(), new PdfMargins(0));
            doc.getPages().get(i).createTemplate().draw(page, new Point2D.Float(0, 0));
        }

        //Save the result file
        newDoc1.saveToFile("split/Doc1.pdf");

        //Create a new PDF file
        PdfDocument newDoc2 = new PdfDocument();

        //Add 3 pages to the new PDF, and draw the content of page 3-5 of the original PDF to the newly added pages
        for (int i = 4; i < 10; i++) {
            page = newDoc2.getPages().add(doc.getPages().get(i).getSize(), new PdfMargins(0));
            doc.getPages().get(i).createTemplate().draw(page, new Point2D.Float(0, 0));
        }

        //Save the result file
        newDoc2.saveToFile("split/Doc2.pdf");
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

    public static void checkQRPdf(PDDocument document) throws IOException, TesseractException, NotFoundException, FormatException, ChecksumException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
//            bufferedImage.getSubimage(1860, 50, 550, 300);
            BufferedImage crop = cropImage(bufferedImage, 1860, 50, 550, 300);
            File file = new File(String.format("ToImage-img-%d.png", page));
            ImageIO.write(crop, "png", file);
            String result = readBarcode(file);
            out.append(result);
        }
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
        Result re = null;
        try {
            re = reader.decode(bitmap);
            System.out.println("Barcode text is " + re.getText());
            return re.getText().toString();
        } catch (Exception e) {
            return "reader error";
        }
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
