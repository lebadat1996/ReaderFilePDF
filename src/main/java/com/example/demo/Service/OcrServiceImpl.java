package com.example.demo.Service;

import com.example.demo.Entity.OcrResult;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OcrServiceImpl {
    OcrResult result(MultipartFile file) throws IOException, TesseractException;
}
