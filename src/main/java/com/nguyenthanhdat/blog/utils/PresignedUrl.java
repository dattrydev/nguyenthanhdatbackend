package com.nguyenthanhdat.blog.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PresignedUrl {

    private final Region region;
    private final AwsCredentialsProvider credentialsProvider;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String createPresignedGetUrl(String keyName) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(region)
                .credentialsProvider(credentialsProvider)
                .build()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            Logger.getGlobal().info("Presigned URL: " + presignedRequest.url());

            return presignedRequest.url().toExternalForm();
        } catch (Exception e) {
            Logger.getGlobal().severe("Error creating presigned URL: " + e.getMessage());
            return "";
        }
    }

    public String convertKeyToPresignedUrl(String content) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']([^\"']*)[\"'][^>]*>");
        Matcher matcher = pattern.matcher(content);

        StringBuilder updatedContent = new StringBuilder();

        while (matcher.find()) {
            String imagePath = matcher.group(1);
            String presignedUrl = createPresignedGetUrl(imagePath);
            matcher.appendReplacement(updatedContent, matcher.group(0).replace(matcher.group(1), presignedUrl));
        }

        matcher.appendTail(updatedContent);

        return updatedContent.toString();
    }

    public String convertPresignedUrlToKey(String content) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']([^\"']*)[\"'][^>]*>");
        Matcher matcher = pattern.matcher(content);

        StringBuilder updatedContent = new StringBuilder();

        while (matcher.find()) {
            String presignedUrl = matcher.group(1);

            String key = presignedUrl.split("\\?")[0];

            key = key.replace("https://nguyenthanhdat-blog-images.s3.ap-southeast-1.amazonaws.com/", "");

            key = URLDecoder.decode(key, StandardCharsets.UTF_8);

            matcher.appendReplacement(updatedContent, matcher.group(0).replace(matcher.group(1), key));
        }

        matcher.appendTail(updatedContent);

        return updatedContent.toString();
    }

}
