package com.nguyenthanhdat.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

    private final AwsCredentialsConfig awsCredentialsConfig;

    @Bean
    public Region awsRegion() {
        return Region.AP_SOUTHEAST_1;
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        awsCredentialsConfig.getAccessKey(),
                        awsCredentialsConfig.getSecretKey()
                )
        );
    }

    @Bean
    public S3Client s3Client(Region region, AwsCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}
