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
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
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
            throws WriterException, IOException,
            NotFoundException, FormatException, ChecksumException {

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

        //readBarCode
//        File file = new File("D:\\demo\\ToImage-img-0.png");
//        readBarcode(file);


        InputStream pdfIs = new FileInputStream("C:/Users/Admin/Downloads/L.C_V1.2.pdf");
        RandomAccessBufferedFileInputStream rbfi = new RandomAccessBufferedFileInputStream(pdfIs);


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

    public static void readBarcode(File file) throws IOException, FormatException, ChecksumException, NotFoundException {
        InputStream barCodeInputStream = new FileInputStream(file);
        BufferedImage barCodeBufferedImage = ImageIO.read(barCodeInputStream);

        LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        Result re = reader.decode(bitmap);
        System.out.println("Barcode text is " + re.getText());
    }
}
