package com.example.common.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

  @Value("${AWS_ACCESS_KEY_ID}")
  private String awsAccessKeyId;

  @Value("${AWS_SECRET_ACCESS_KEY}")
  private String awsSecretAccessKey;

  @Bean
  public AmazonS3 amazonS3() {
    BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
    return AmazonS3ClientBuilder.standard()
            .withRegion(Regions.US_EAST_1)
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .build();
  }
}
