package com.barshid.schematech;

//import com.barshid.schematech.model.bo.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Configuration
public class CreatBean {
    @Autowired
    private Environment env;


    public CreatBean() {
    }

//    @Bean
//    public User getUser() {
//        return new User();
//    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    //    @Bean
//    public AmazonS3 getAwsS3(){
//        BasicAWSCredentials creds = new BasicAWSCredentials(env.getProperty("schematek.aws_access_key"), env.getProperty("schematek.aws_secret_key"));
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.US_WEST_2)
//                .withCredentials(new AWSStaticCredentialsProvider(creds))
//                .build();
//    }
    @Value("${clientUrl}")
    private String clientUrl;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(clientUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
