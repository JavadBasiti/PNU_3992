package com.barshid.schematech.model.bo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
//@Embeddable
public class Media implements Serializable {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MediaType type;
    private String mediaUrl;
    private String smallThumbUrl;
    private String mediumThumbUrl;
    private String largeThumbUrl;
    private String parameters;
    private Integer ordering;
    private Timestamp uploadTime;
    private String mimeType;
    private Integer width;
    private Integer height;

}
