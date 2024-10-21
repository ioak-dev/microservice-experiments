package com.hexagonal.user.infrastructure.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class S3Config {

  @Value("${aws.accessKeyId}")
  private String accessKey;

  @Value("${aws.secretKey}")
  private String secretKey;

  @Value("${aws.region}")
  private String region;

  public AmazonS3 getAmazonS3Cient() {
    try {
      if (accessKey != null && secretKey != null) {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withRegion(region)
            .build();
      } else {
        return AmazonS3ClientBuilder
            .standard()
            .withRegion(region)
            .build();
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }
  }
}
