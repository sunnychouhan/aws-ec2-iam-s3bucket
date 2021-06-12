package com.chousun.awsec2iams3bucket;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.Objects.nonNull;

@RestController
@Slf4j
@RequestMapping("/")
public class Controller {

    @PostMapping(value = "getS3Object", consumes = "application/json")
    public ResponseEntity<String> getS3Object(@RequestBody S3Bucket s3Bucket) {
        log.info("Started gets3Object with path variable [s3Bucket={}] ", s3Bucket);
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        StringBuilder s3FileContent = null;
        try {
            log.info("Downloading an object");
            S3Object s3object = s3Client.getObject(
                    new GetObjectRequest(s3Bucket.name, s3Bucket.key));
            InputStream delegateStream = s3object.getObjectContent().getDelegateStream();
            s3FileContent = displayTextInputStream(delegateStream);
        } catch (AmazonServiceException ase) {
            log.error("Exception was thrown by the service: {}", ase);
        } catch (AmazonClientException ace) {
            log.error("Exception was thrown by the client: {}", ace);
        } catch (IOException e) {
            log.error("Exception in displayTextInputStream : {}", e);
        }
        log.info("File Content Retrieved Successfully : {}", s3FileContent);
        String body = nonNull(s3FileContent) ? s3FileContent.toString() : null;
        return ResponseEntity.ok(body);
    }

    public static StringBuilder displayTextInputStream(InputStream input) throws IOException {
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            } else stringBuilder = stringBuilder.append(line).append("\n");
            log.info("    " + line);
        }
        return stringBuilder;
    }
}
