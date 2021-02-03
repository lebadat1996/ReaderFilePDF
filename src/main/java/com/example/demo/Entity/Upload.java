package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class Upload {
    private MultipartFile file;
}
