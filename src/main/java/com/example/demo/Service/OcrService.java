package com.example.demo.Service;


import com.example.demo.Entity.OcrResult;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class OcrService implements OcrServiceImpl {
    @Autowired
    Tesseract tesseract;

    @Override
    public OcrResult result(MultipartFile file) throws IOException, TesseractException {
        File convFile = convert(file);
        tesseract.setLanguage("vie");
        String text = tesseract.doOCR(convFile);
        OcrResult ocrResult = new OcrResult();
        ocrResult.setResult(text);
        System.out.println(ocrResult.getResult());
        return ocrResult;
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


}
