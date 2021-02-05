package com.example.demo;

import com.lowagie.text.pdf.PdfReader;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    Tesseract getTesseract() {
        Tesseract tesseract = Tesseract.getInstance();
        tesseract.setLanguage("eng");
        tesseract.setDatapath("./tessdata");
        return tesseract;
    }

}
