package com.sitix.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile multipartFile, String id);
    byte[] loadImage(String fileName);
    byte[] downloadFile(String fileName);

}
