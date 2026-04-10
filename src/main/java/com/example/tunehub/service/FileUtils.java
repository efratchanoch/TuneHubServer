
package com.example.tunehub.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;

public class FileUtils {
    private static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\media";
    private static String IMAGES_FOLDER = "images";
    private static String AUDIO_FOLDER = "audio";
    private static String VIDEO_FOLDER = "video";
    private static String DOCUMENTS_FOLDER = "docs";

    // Generic

    /**
     * Generate a unique file name using UUID and preserve the original extension.
     */
    public static String generateUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex != -1) {
            fileExtension = originalFileName.substring(lastDotIndex);
        }
        return UUID.randomUUID().toString() + fileExtension;
    }

    // Images

    /**
     * Save an image file on disk.
     */
    public static void uploadImage(MultipartFile file, String uniqueFileName) throws IOException {
        Path fileName = Paths.get(UPLOAD_DIRECTORY, IMAGES_FOLDER, uniqueFileName);
        Files.createDirectories(fileName.getParent());
        Files.write(fileName, file.getBytes());
    }

    /**
     * Convert an image to Base64 string.
     */
    public static String imageToBase64(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        try {
            Path fullPath = Paths.get(UPLOAD_DIRECTORY, IMAGES_FOLDER, fileName);
            byte[] byteImage = Files.readAllBytes(fullPath);

            return Base64.getEncoder().encodeToString(byteImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<String> imagesToBase64(List<String> fileNames) {
        if (fileNames == null) return List.of();
        return fileNames.stream()
                .map(FileUtils::imageToBase64)
                .filter(b -> b != null)
                .toList();
    }


    // Audio

    /**
     * Saves the uploaded audio file with a unique name.
     *
     * @param file            MultipartFile received from the request
     * @param uniqueAudioName String unique filename (UUID + extension)
     * @throws IOException if saving fails
     */
    public static void uploadAudio(MultipartFile file, String uniqueAudioName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIRECTORY, AUDIO_FOLDER, uniqueAudioName);
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath.toFile());
    }

    /**
     * Returns an InputStreamResource for streaming the audio file.
     *
     * @param fileName name of the saved audio file
     * @return InputStreamResource ready for ResponseEntity
     * @throws IOException if reading the file fails
     */
    public static InputStreamResource getAudio(String fileName) throws IOException {
        Path path = Paths.get(UPLOAD_DIRECTORY, AUDIO_FOLDER, fileName);
        InputStream stream = Files.newInputStream(path);
        return new InputStreamResource(stream);
    }

    // Video

    /**
     * Saves the uploaded video file with a unique name.
     *
     * @param file            MultipartFile received from the request
     * @param uniqueVideoName String unique filename (UUID + extension)
     * @throws IOException if saving fails
     */
    public static void uploadVideo(MultipartFile file, String uniqueVideoName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIRECTORY, VIDEO_FOLDER, uniqueVideoName);
        file.transferTo(filePath.toFile());
    }

    /**
     * Returns an InputStreamResource for streaming the video file.
     *
     * @param fileName name of the saved video file
     * @return InputStreamResource ready for ResponseEntity
     * @throws IOException if reading the file fails
     */
    public static InputStreamResource getVideo(String fileName) throws IOException {
        Path path = Paths.get(UPLOAD_DIRECTORY, VIDEO_FOLDER, fileName);
        InputStream stream = Files.newInputStream(path);
        return new InputStreamResource(stream);
    }

    // Document

    /**
     * Save document (PDF or other) to disk.
     */
    public static String uploadDocument(MultipartFile file, String uniqueFileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, uniqueFileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return uniqueFileName;
    }

    /**
     * Get number of pages in a PDF document.
     */
    public static int getPDFPageCount(byte[] pdfBytes) throws IOException {
        if (pdfBytes == null || pdfBytes.length == 0) return 0;
        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfBytes))) {
            return document.getNumberOfPages();
        }
    }


    /**
     * Stream document file to client (recommended for multiple or large PDFs).
     */
    public static InputStreamResource getDocument(String fileName) throws IOException {
        Path path = Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, fileName);
        InputStream stream = Files.newInputStream(path);
        return new InputStreamResource(stream);
    }


    /**
     * Convert an image to Base64 string.
     */
    public static String documentToBase64(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        try {
            Path fullPath = Paths.get(UPLOAD_DIRECTORY, DOCUMENTS_FOLDER, fileName);
            byte[] byteImage = Files.readAllBytes(fullPath);

            return Base64.getEncoder().encodeToString(byteImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Downloads an image from a specified URL and saves it to a local directory.
     * This method generates a unique filename using UUID to prevent collisions
     * and persists the byte stream to the IMAGES_FOLDER directory.
     *
     * @param imageUrl The absolute URL of the image to be downloaded.
     * @return The uniquely generated filename (including extension) of the saved image.
     * @throws IOException If an I/O error occurs during connection or file writing.
     */
    public static String downloadAndSaveImage(String imageUrl) throws IOException {

        // Generate a unique identifier for the filename to ensure no overwrites
        String fileName = UUID.randomUUID().toString() + ".jpg";

        // Define the destination path within the IMAGES_FOLDER folder
        Path targetPath = Paths.get(UPLOAD_DIRECTORY, IMAGES_FOLDER, fileName);


        // Open an input stream from the URL and copy the data to the target path
        // The try-with-resources block ensures the stream is closed automatically
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }
}


