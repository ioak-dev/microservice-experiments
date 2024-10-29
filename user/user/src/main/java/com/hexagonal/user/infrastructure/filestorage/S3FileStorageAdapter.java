package com.hexagonal.user.infrastructure.filestorage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.hexagonal.user.application.port.LocalFileStoragePort;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hexagonal.user.infrastructure.config.S3Config;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@Profile("s3")
@Slf4j
public class S3FileStorageAdapter implements LocalFileStoragePort {

  @Value("${aws.s3.bucket}")
  public String bucketName;

  @Autowired
  private S3Config s3Config;
  private final ExecutorService executorService = Executors.newFixedThreadPool(5);
  @Override
  public Future<String> uploadFile(MultipartFile file, String userId) {
    return executorService.submit(() -> {
      try {
        if (bucketName != null) {
          File fileToUpload = convertMultiPartFileToFile(file);
          StringBuilder url = new StringBuilder().append("/").append(fileToUpload.getName());
          PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, url.toString(), fileToUpload);
          s3Config.getAmazonS3Cient().putObject(putObjectRequest);

          if (fileToUpload.exists()) {
            fileToUpload.delete();
          }
          log.info("Deleted the file from root directory");

          return s3Config.getAmazonS3Cient().getUrl(bucketName, url.toString()).toString();
        }
      } catch (Exception e) {
        log.error("Error occurred during file upload", e);
        throw new RuntimeException(e.getMessage());
      }
      return userId;
    });
  }

  private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
    File file = new File(multipartFile.getOriginalFilename());
    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(multipartFile.getBytes());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return file;
  }
  @Override
  public Future<byte[]> downloadFile(String fileName, String userId) {
    return executorService.submit(() -> {
      try {
        com.amazonaws.services.s3.model.S3Object s3Object = s3Config.getAmazonS3Cient().getObject(bucketName, fileName);
        return IOUtils.toByteArray(s3Object.getObjectContent());
      } catch (IOException e) {
        log.error("Failed to download file from S3", e);
        throw new RuntimeException("Failed to download file from S3", e);
      }
    });
  }

  @Override
  public Future<Void> deleteFile(String fileName, String userId) {
    return executorService.submit(() -> {
      s3Config.getAmazonS3Cient().deleteObject(bucketName, fileName);
      return null;
    });
  }

}

