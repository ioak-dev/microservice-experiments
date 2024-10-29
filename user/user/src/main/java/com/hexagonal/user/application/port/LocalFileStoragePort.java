package com.hexagonal.user.application.port;

import java.util.concurrent.Future;
import org.springframework.web.multipart.MultipartFile;


public interface LocalFileStoragePort {
  Future<String> uploadFile(MultipartFile file, String userId);
  Future<byte[]> downloadFile(String fileName, String userId);
  Future<Void> deleteFile(String fileName, String userId);
}
