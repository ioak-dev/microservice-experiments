package com.ride.Goober.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {


  @Value("${aws.credentials.access-key}")
  private String accessKey;

  @Value("${aws.credentials.secret-key}")
  private String secretKey;

  @Value("${aws.region}")
  private String region;

  @Bean
  SqsAsyncClient sqsAsyncClient(){
    return SqsAsyncClient
        .builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider
            .create(AwsBasicCredentials.create(accessKey, secretKey)))
        .build();
  }

  @Bean
  public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient){
    return SqsTemplate.builder().sqsAsyncClient(sqsAsyncClient).build();
  }

  @Bean
  public SqsClient sqsClient() {
    return SqsClient.builder()
        .credentialsProvider(
            StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .region(Region.of(region))
        .build();
  }

  @PostConstruct
  public void setRegion(){
    System.setProperty("aws.region","us-east-1");
  }


}
