package com.kibikalo.pastebin.service;

import com.kibikalo.pastebin.exceptions.S3ServiceException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Getter
@Service
public class S3Service {

    private final PasteNameGenerator pasteNameGenerator;
    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private static final String ACCESS_KEY = System.getenv("AWS_S3_ACCESS_KEY");
    private static final String SECRET_KEY = System.getenv("AWS_S3_SECRET_KEY");
    private static final Region REGION = Region.EU_NORTH_1;
    private static final String BUCKET_NAME = System.getenv("AWS_S3_BUCKET_NAME");
    private static final String FOLDER_NAME = "pastes";

    private static S3Client s3Client;
    private String objectPath;

    @Autowired
    public S3Service(PasteNameGenerator pasteNameGenerator) {
        this.pasteNameGenerator = pasteNameGenerator;
        configureS3Client();
    }

    private void configureS3Client() {
        // Create AWS credentials using access key and secret key
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);

        // Create an S3 client builder and configure credentials and region
        s3Client = S3Client.builder()
                .region(REGION)
                .credentialsProvider(() -> credentials)
                .build();
    }

    public void savePaste(String content, String objectName) {
        objectPath = FOLDER_NAME + "/" + objectName;

        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(objectPath)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, content.length()));
        } catch (Exception e) {
            String errorMessage = "Error trying to save file to S3: " + e.getMessage();
            logger.error(errorMessage, e);
            throw new S3ServiceException(errorMessage, e);
        }
    }

    public String getPaste(String objectPath) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(objectPath)
                    .build();

            ResponseInputStream<GetObjectResponse> s3objectResponse = s3Client.getObject(getObjectRequest);

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3objectResponse))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("");
                }
            }

            return content.toString();

        } catch (Exception e) {
            String errorMessage = "Error reading text file from S3: " + e.getMessage();
            logger.error(errorMessage, e);
            throw new S3ServiceException(errorMessage, e);
        }
    }

    public String getObjectUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        return s3Client.utilities().getUrl(builder -> builder
                .region(REGION)
                .bucket(BUCKET_NAME)
                .key(key)
                .build()).toString();
    }
}
