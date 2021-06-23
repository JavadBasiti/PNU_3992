package com.barshid.schematech;

import com.barshid.schematech.model.bo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Slf4j
@Configuration
public class CreatBean {
    @Autowired
    private Environment env;


    public CreatBean() {
    }

    @Bean
    public User getUser() {
        return new User();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AmazonS3 getAwsS3(){
//        BasicAWSCredentials creds = new BasicAWSCredentials(env.getProperty("schematek.aws_access_key"), env.getProperty("schematek.aws_secret_key"));
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.US_WEST_2)
//                .withCredentials(new AWSStaticCredentialsProvider(creds))
//                .build();
//    }

}
