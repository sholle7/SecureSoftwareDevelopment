package com.zuehlke.securesoftwaredevelopment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private static final Path BASE_DIR = Paths.get("src/main/resources/static/images").toAbsolutePath().normalize();
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif"));

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadZip(
            @RequestParam("filename") String filename,
            @RequestParam("model") String model,
            @RequestParam("manufacturer") String manufacturer,
            @RequestParam("price") String price,
            @RequestParam("wholesalePrice") String wholesalePrice
    ) {
        try {
            if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
                logger.warn("Invalid filename attempt: {}", filename);
                return ResponseEntity.badRequest().build();
            }

            String extension = getFileExtension(filename);
            if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
                logger.warn("File extension not allowed: {}", extension);

                return ResponseEntity.badRequest().build();
            }

            Path imagePath = BASE_DIR.resolve(filename).normalize();
            if (!imagePath.startsWith(BASE_DIR)) {
                logger.warn("Attempted path traversal: {}", filename);

                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            model = sanitizeInput(model);
            manufacturer = sanitizeInput(manufacturer);
            price = sanitizeInput(price);
            wholesalePrice = sanitizeInput(wholesalePrice);

            Path zipPath = Files.createTempFile("car-info-", ".zip");

            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                String carInfo = String.format(
                        "Model: %s%nManufacturer: %s%nPrice: %s%nWholesale Price: %s%n",
                        model, manufacturer, price, wholesalePrice
                );

                zos.putNextEntry(new ZipEntry("car_info.txt"));
                zos.write(carInfo.getBytes());
                zos.closeEntry();

                if (Files.exists(imagePath) && Files.isRegularFile(imagePath)) {
                    zos.putNextEntry(new ZipEntry("car_image" + extension));

                    Files.copy(imagePath, zos);

                    zos.closeEntry();
                }
            }

            byte[] zipBytes = Files.readAllBytes(zipPath);

            Files.deleteIfExists(zipPath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"car_details.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipBytes);

        } catch (IOException e) {
            logger.error("Error creating ZIP file", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');

        return (i > 0) ? fileName.substring(i).toLowerCase() : "";
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }

        return input.replaceAll("[\\r\\n]", "").trim();
    }
}
