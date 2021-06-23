package com.barshid.schematech.web.dto;

import lombok.Data;


@Data
public class MediaTO {
    private String smallThumbUrl;
    private String mediumThumbUrl;
    private String largeThumbUrl;
    private String mediaUrl;
    private String mimeType;
    private Integer width;
    private Integer height;


}
