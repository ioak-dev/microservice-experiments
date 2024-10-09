package com.hexagonal.user.adapters.rest;

import com.hexagonal.user.application.port.LocalFileStoragePort;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public ResponseEntity<String> uploadFile(@PathVariable String userId)
      throws IOException {
    MultipartFile file = getFileAsMultipartFile("10101017640450_Sep2024.pdf");
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

  public MultipartFile getFileAsMultipartFile(String fileName) throws IOException {
    ClassPathResource resource = new ClassPathResource(fileName);
    Path path = resource.getFile().toPath();
    byte[] content = Files.readAllBytes(path);
    return new MockMultipartFile(
        fileName,
        resource.getFilename(),
        Files.probeContentType(path),
        content
    );
  }
}
