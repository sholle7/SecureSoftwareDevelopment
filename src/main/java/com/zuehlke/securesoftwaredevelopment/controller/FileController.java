package com.zuehlke.securesoftwaredevelopment.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class FileController {

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadZip(
            @RequestParam("filename") String filename,
            @RequestParam("model") String model,
            @RequestParam("manufacturer") String manufacturer,
            @RequestParam("price") String price,
            @RequestParam("wholesalePrice") String wholesalePrice
    ) {
        try {
            File baseDir = new File("src/main/resources/static/images").getAbsoluteFile();
            File imageFile = new File(baseDir, filename).getAbsoluteFile();

            Path zipPath = Files.createTempFile("car-info-", ".zip");

            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {

                String carInfo = String.format(
                        "Model: %s%nManufacturer: %s%nPrice: %s%nWholesale Price: %s%n",
                        model, manufacturer, price, wholesalePrice
                );

                zos.putNextEntry(new ZipEntry("car_info.txt"));

                zos.write(carInfo.getBytes());

                zos.closeEntry();

                if (imageFile.exists()) {
                    zos.putNextEntry(new ZipEntry("car_image" + getFileExtension(filename)));

                    Files.copy(imageFile.toPath(), zos);

                    zos.closeEntry();
                }
            }

            byte[] zipBytes = Files.readAllBytes(zipPath);

            Files.deleteIfExists(zipPath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"car_details.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipBytes);

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(500).build();
        }
    }

    private String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i) : "";
    }
}