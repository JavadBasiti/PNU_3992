package com.barshid.schematech.service;

import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;

@Service
public class CdnService {

    public void putObject(String bucketName, String key, InputStream inputStream, ObjectMetadata metadata) {
    }

    public String generatePresignedUrl(String bucketName, String smallThumbUrl, Date expiration, HttpMethod get) {
        return "";
    }
}


@Data
class ObjectMetadata{
    private Long ContentLength;
}