package com.nguyenthanhdat.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cloud.aws.credentials")
public class AwsCredentialsConfig {
    private String accessKey;
    private String secretKey;
}
