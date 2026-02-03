package uk.ac.ed.inf.acpAssignment.controller;
import java.net.URL;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.nio.file.Paths;
import software.amazon.awssdk.services.s3.S3Client;
import java.net.URI;
import static uk.ac.ed.inf.acpAssignment.configuration.SystemEnvironment.ACCESS_KEY;
import static uk.ac.ed.inf.acpAssignment.configuration.SystemEnvironment.SECRET_KEY;
import static uk.ac.ed.inf.acpAssignment.configuration.SystemEnvironment.AWS_REGION;

public class S3Controller {

  S3Client s3Client = S3Client.builder()
      .endpointOverride(URI.create("https://s3.localhost.localstack.cloud:4566"))
      .credentialsProvider(StaticCredentialsProvider.create(
          AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY)))
      .region(AWS_REGION)
      .build();

  String bucketName = "s2417814";

  public void fillBucket(String objectKey, URL url) {
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .build();

    PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
        Paths.get("src/main/resources/" + objectKey));
  }


}
