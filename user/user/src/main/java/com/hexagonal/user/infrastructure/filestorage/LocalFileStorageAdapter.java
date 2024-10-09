package com.hexagonal.user.infrastructure.filestorage;

import com.hexagonal.user.application.port.LocalFileStoragePort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Service
@Profile("local")
public class LocalFileStorageAdapter implements LocalFileStoragePort {

  private String storagePath = "C:\\";

  @Override
  public String uploadFile(MultipartFile file, String userId) {
    try {
      String fileName = userId + "_" + file.getOriginalFilename();
      Path filePath = Paths.get(storagePath + fileName);
      Files.createDirectories(filePath.getParent());
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      return fileName;
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload file", e);
    }
  }

  @Override
  public byte[] downloadFile(String fileName, String userId) {
    try {
      Path filePath = Paths.get(storagePath + fileName);
      return Files.readAllBytes(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to download file", e);
    }
  }

  @Override
  public void deleteFile(String fileName, String userId) {
    try {
      Path filePath = Paths.get(storagePath + fileName);
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete file", e);
    }
  }
}
