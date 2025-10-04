package com.zuehlke.securesoftwaredevelopment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Files;

@Controller
public class FileController {

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename) {
        try {
            File baseDir = new File("src/main/resources/static/images").getAbsoluteFile();
            File file = new File(baseDir, filename).getAbsoluteFile();

            byte[] content = Files.readAllBytes(file.toPath());

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")
                    .body(content);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}