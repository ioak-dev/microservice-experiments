package com.hexagonal.user.adapters.rest;

import com.hexagonal.user.application.port.LocalFileStoragePort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/{userId}/files")
public class UserFileController {

  private final LocalFileStoragePort localFileStoragePort;

  public UserFileController(LocalFileStoragePort localFileStoragePort) {
    this.localFileStoragePort = localFileStoragePort;
  }

  @PostMapping(value = "/upload")
  public ResponseEntity<String> uploadFile(@PathVariable String userId, @RequestParam("file") MultipartFile file)
      throws IOException {
    String fileName = localFileStoragePort.uploadFile(file, userId);
    return ResponseEntity.ok("File uploaded: " + fileName);
  }

  @GetMapping("/download/{fileName}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable String userId, @PathVariable String fileName) {
    byte[] fileData = localFileStoragePort.downloadFile(fileName, userId);
    return ResponseEntity.ok(fileData);
  }

  @DeleteMapping("/delete/{fileName}")
  public ResponseEntity<Void> deleteFile(@PathVariable String userId, @PathVariable String fileName) {
    localFileStoragePort.deleteFile(fileName, userId);
    return ResponseEntity.noContent().build();
  }
}
