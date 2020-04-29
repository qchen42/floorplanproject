package com.example.project.service;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {
    public void uploadToS3(File file, String remoteFieldName) {
        String AWS_ACCESS_KEY = "AKIAI7HSVBIJC4KII6HA";
        String AWS_SECRET_KEY = "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";
        String bucketName = "projectfloorplanjasonchen";
        String key = remoteFieldName + ".png";

        BasicAWSCredentials awsCredentials  = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_WEST_2)
                .build();
        try {
            long contentLength = file.length();
            long partSize = 5 * 1024 * 1024;
            List<PartETag> partETags = new ArrayList<PartETag>();
            // Initiate the multipart upload.
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(
                    bucketName, key);
            InitiateMultipartUploadResult initResponse = s3.initiateMultipartUpload(initRequest);

            // Upload the file parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Because the last part could be less than 5 MB, adjust the
                // part size as needed.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create the request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucketName).withKey(key)
                        .withUploadId(initResponse.getUploadId())
                        .withPartNumber(i).withFileOffset(filePosition)
                        .withFile(file).withPartSize(partSize);

                // Upload the part and add the response's ETag to our list.
                UploadPartResult uploadResult = s3.uploadPart(uploadRequest);
                partETags.add(uploadResult.getPartETag());

                filePosition += partSize;
            }

            // Complete the multipart upload.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
                    bucketName, key, initResponse.getUploadId(), partETags);
            s3.completeMultipartUpload(compRequest);
        } catch (AmazonClientException ex) {
            ex.printStackTrace();
        }
    }
}
