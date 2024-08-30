package com.sitix.model.service.Impl;

import com.sitix.configuration.FileStorageProperties;
import com.sitix.model.service.FileStorageService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path storageLocation;

    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.storageLocation = Paths.get(fileStorageProperties.getUpLoadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(storageLocation);
        } catch (
                IOException e) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }


    @Override
    public String storeFile(MultipartFile file, String id) {
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        String idFileName = id + "_" + fileName;

        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid Path sequence" + fileName);
            }
            Path targetLocation = storageLocation.resolve(idFileName);
            Files.write(targetLocation, file.getBytes());
            return idFileName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public byte[] loadImage(String fileName) {
        String filename = null;
        try {

//            filename = "assets/images/"  + fileName;
            Path filePath = storageLocation.resolve(fileName).normalize();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(filePath.toFile())
                    .size(1000, 1000)
                    .keepAspectRatio(true)
                    .outputFormat("jpg")
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            Path filePath = storageLocation.resolve(fileName).normalize();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not download file " + fileName + ". Please try again!", e);
        }
    }
}
