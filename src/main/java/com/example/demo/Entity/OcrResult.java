package com.example.demo.Entity;

import lombok.Data;

@Data
public class OcrResult {
    private String result;

    @Override
    public String toString() {
        return "OcrResult{" +
                "result='" + result + '\'' +
                '}';
    }
}
