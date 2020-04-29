package com.example.project.service;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.Region;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
public class S3Service {
    public String uploadToS3(File tempFile, String remoteFieldName) {
        String AWS_ACCESS_KEY = "";
        String AWS_SECRET_KEY = "";
        String bucketName = "";

        BasicAWSCredentials awsCredentials  = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(String.valueOf(Region.US_East_2))
                .build();
        try {
            String bucketPath = bucketName + "/upload";
            s3.putObject(new PutObjectRequest(bucketPath, remoteFieldName, tempFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
            GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, remoteFieldName);
            URL url = s3.generatePresignedUrl(urlRequest);
            return url.toString();
        } catch (AmazonClientException sex) {
            sex.printStackTrace();
        }
        return null;
    }
}
