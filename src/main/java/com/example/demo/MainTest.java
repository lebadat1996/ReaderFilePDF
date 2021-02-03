package com.example.demo;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainTest {


    public static void main(String[] args) throws Exception {
        MainTest mainTest= new MainTest();
        mainTest.run();
    }

    private void run() throws Exception {
        PDDocument document = PDDocument.load(new File("E:\\PdfFile\\Demo.pdf"));

    }


}
