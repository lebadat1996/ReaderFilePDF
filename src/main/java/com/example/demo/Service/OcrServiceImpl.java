package com.example.demo.Service;

import com.example.demo.Entity.DataOrc;
import com.example.demo.Entity.OcrResult;
import net.sourceforge.tess4j.TesseractException;
import org.ghost4j.document.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OcrServiceImpl {
    DataOrc result(MultipartFile file) throws IOException, TesseractException, DocumentException;
}
