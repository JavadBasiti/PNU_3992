package com.barshid.schematech.controller.dto;

import com.barshid.schematech.model.bo.Media;
import com.barshid.schematech.service.CdnService;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.Date;

@Data
public class MediaTO {
    private String smallThumbUrl;
    private String mediumThumbUrl;
    private String largeThumbUrl;
    private String mediaUrl;
    private String mimeType;
    private Integer width;
    private Integer height;

//    public MediaTO() {
//    }

    public MediaTO(Media media, String bucketName, CdnService cdnService) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;//one hour
        expiration.setTime(expTimeMillis);
        smallThumbUrl = cdnService.generatePresignedUrl(bucketName,media.getSmallThumbUrl(),expiration, HttpMethod.GET).toString();
        mediumThumbUrl= cdnService.generatePresignedUrl(bucketName,media.getMediumThumbUrl(),expiration,HttpMethod.GET).toString();
        largeThumbUrl = cdnService.generatePresignedUrl(bucketName,media.getLargeThumbUrl(),expiration,HttpMethod.GET).toString();
        mediaUrl      = cdnService.generatePresignedUrl(bucketName,media.getMediaUrl(),expiration,HttpMethod.GET).toString();
        mimeType      = media.getMimeType();
        width         = media.getWidth();
        height        = media.getHeight();
    }
}
