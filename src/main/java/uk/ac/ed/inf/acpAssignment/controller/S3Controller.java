package uk.ac.ed.inf.acpAssignment.controller;

import java.net.URI;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.S3Object;
import uk.ac.ed.inf.acpAssignment.configuration.S3Configuration;
import uk.ac.ed.inf.acpAssignment.configuration.SystemEnvironment;

@RestController()
@RequestMapping("/api/v1/acp/s3")
public class S3Controller {

  private final S3Configuration s3Configuration;
  private final SystemEnvironment systemEnvironment;

  public S3Controller(S3Configuration s3Configuration, SystemEnvironment systemEnvironment) {
    this.s3Configuration = s3Configuration;
    this.systemEnvironment = systemEnvironment;
  }


  @GetMapping("/endpoint")
  public String getS3Endpoint() {
    return s3Configuration.getS3Endpoint();
  }

  @GetMapping("/buckets")
  public List<String> listBuckets() {
    return getS3Client().listBuckets().buckets().stream().map(Bucket::name).toList();
  }

  @GetMapping("/list-objects/{bucket}")
  public List<String> listBucketObjects(@PathVariable String bucket) {
    return getS3Client().listObjectsV2(b -> b.bucket(bucket)).contents().stream().map(S3Object::key).toList();
  }

  @PutMapping("/create-bucket/{bucket}")
  public void createBucket(@PathVariable String bucket) {
    getS3Client().createBucket(b -> b.bucket(bucket));
  }

  @PutMapping("/create-object/{bucket}/{s3Object}")
  public void createBucket(@PathVariable String bucket, @PathVariable String s3Object, @RequestBody String objectContent) {
    getS3Client().putObject(b -> b.bucket(bucket).key(s3Object), software.amazon.awssdk.core.sync.RequestBody.fromString(objectContent));
  }

  private S3Client getS3Client() {
    return S3Client.builder()
        .endpointOverride(URI.create(s3Configuration.getS3Endpoint()))
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(systemEnvironment.getAwsUser(), systemEnvironment.getAwsSecret())))
        .region(systemEnvironment.getAwsRegion())
        .build();
  }

}
