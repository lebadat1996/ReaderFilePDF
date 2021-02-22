package com.example.demo.Controller;

import com.example.demo.Entity.DataOrc;
import com.example.demo.Entity.OcrResult;

import com.example.demo.Entity.Upload;
import com.example.demo.Service.OcrService;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.ghost4j.document.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.List;

@Controller
public class OcrController {
    @Autowired
    private OcrService ocrService;

    @GetMapping("/show")
    public ModelAndView showForm() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("data", new DataOrc());
        return modelAndView;
    }

    @PostMapping("/uploadFile")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) throws DocumentException, IOException, TesseractException, NotFoundException, FormatException, ChecksumException {
        List<DataOrc> dataOrc = ocrService.result(file);
        ModelAndView modelAndView = new ModelAndView("index");
        if (dataOrc != null) {
            modelAndView.addObject("message", "success");
            modelAndView.addObject("data", dataOrc);
        } else {
            modelAndView.addObject("message", "no success");
        }
        return modelAndView;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<DataOrc>> upload(@RequestParam("file") MultipartFile file) throws IOException, TesseractException, DocumentException, NotFoundException, FormatException, ChecksumException {
        return new ResponseEntity<>(ocrService.result(file), HttpStatus.OK);
    }
}
