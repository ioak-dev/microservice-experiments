package com.hexagonal.user.application.port;

import org.springframework.web.multipart.MultipartFile;


public interface LocalFileStoragePort {
  String uploadFile(MultipartFile file, String userId);
  byte[] downloadFile(String fileName, String userId);
  void deleteFile(String fileName, String userId);
}
