package com.hexagonal.user.infrastructure.filestorage;

import com.hexagonal.user.application.port.LocalFileStoragePort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Service
@Profile("local")
public class LocalFileStorageAdapter implements LocalFileStoragePort {

  private String storagePath = "C:\\";
  private final ExecutorService executorService = Executors.newFixedThreadPool(5);
  @Override
  public Future<String> uploadFile(MultipartFile file, String userId) {
    String fileName = userId + "_" + file.getOriginalFilename();
    return executorService.submit(() -> {
      try {
        Path filePath = Paths.get(storagePath + fileName);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        throw new RuntimeException("Failed to upload file", e);
      }
      return fileName;
    });
  }

  @Override
  public Future<byte[]> downloadFile(String fileName, String userId) {
    return executorService.submit(()-> {
      try {
        Path filePath = Paths.get(storagePath + fileName);
        return Files.readAllBytes(filePath);
      } catch (IOException e) {
        throw new RuntimeException("Failed to download file", e);
      }
    });
  }

  @Override
  public Future<Void> deleteFile(String fileName, String userId) {
    return executorService.submit(()->{
    try {
      Path filePath = Paths.get(storagePath + fileName);
      Files.deleteIfExists(filePath);
      return null;
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete file", e);
    }
    });
  }
}
