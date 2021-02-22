package com.example.demo.Service;

import com.example.demo.Entity.DataOrc;
import com.example.demo.Entity.OcrResult;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import net.sourceforge.tess4j.TesseractException;
import org.ghost4j.document.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OcrServiceImpl {
    List<DataOrc> result(MultipartFile file) throws IOException, TesseractException, DocumentException, NotFoundException, FormatException, ChecksumException;
}
