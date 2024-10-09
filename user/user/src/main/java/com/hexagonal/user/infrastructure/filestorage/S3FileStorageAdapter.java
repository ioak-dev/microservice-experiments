package com.hexagonal.user.infrastructure.filestorage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;
import com.hexagonal.user.application.port.LocalFileStoragePort;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Profile("s3")
public class S3FileStorageAdapter implements LocalFileStoragePort {

  private AmazonS3 s3Client;
  @Value("${aws.s3.bucket}")
  public String bucketName;

  public S3FileStorageAdapter(AmazonS3 s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public String uploadFile(MultipartFile file, String userId) {
    try {
      String fileName = userId + "_" + file.getOriginalFilename();
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(file.getSize());
      s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
      return fileName;
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload file to S3", e);
    }
  }

  @Override
  public byte[] downloadFile(String fileName, String userId) {
    try {
      com.amazonaws.services.s3.model.S3Object s3Object = s3Client.getObject(bucketName, fileName);
      return IOUtils.toByteArray(s3Object.getObjectContent());
    } catch (IOException e) {
      throw new RuntimeException("Failed to download file from S3", e);
    }
  }

  @Override
  public void deleteFile(String fileName, String userId) {
    s3Client.deleteObject(bucketName, fileName);
  }
}

