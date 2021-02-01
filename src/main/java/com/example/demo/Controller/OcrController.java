package com.example.demo.Controller;

import com.example.demo.Entity.OcrResult;

import com.example.demo.Service.OcrService;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
public class OcrController {
    @Autowired
    private OcrService ocrService;

    @PostMapping("/upload")
    public ResponseEntity<OcrResult> upload(@RequestParam("file") MultipartFile file) throws IOException, TesseractException {
        try {
            File fileDir = new File("C:\\Users\\Admin\\Desktop\\DemoPDF.txt");
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileDir), "UTF8"));
            String result = ocrService.result(file).getResult();
            for (int i = 0; i < result.length(); i++) {
                char Kt = result.charAt(i);
                if (Character.isSpace(Kt)) {
                    out.append("\r\n");
                } else {
                    out.append(Kt);
                }
            }
            out.flush();
            out.close();

        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(ocrService.result(file));
    }
}
