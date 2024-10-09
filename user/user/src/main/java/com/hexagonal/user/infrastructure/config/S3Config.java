package com.hexagonal.user.infrastructure.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

  @Value("${aws.accessKeyId}")
  private String awsId;

  @Value("${aws.secretKey}")
  private String awsKey;

  @Value("${aws.region}")
  private String region;

  @Bean
  public AmazonS3 amazonS3() {
    BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
    return AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
        .build();
  }
}
