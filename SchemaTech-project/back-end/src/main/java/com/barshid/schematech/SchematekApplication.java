package com.barshid.schematech;

import com.barshid.schematech.service.DataUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
//@EnableScheduling
public class SchematekApplication {


    private static DataUpdate dataUpdate;
    @Autowired
    public void setDataUpdate(DataUpdate dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public static void main(String[] args) {
        log.info("Main routine Started ...");
        /*ConfigurableApplicationContext ctx =*/SpringApplication.run(SchematekApplication.class, args);
        //first && update Data...
        if (dataUpdate.updateDataVersion()) {
            log.info("Update true Complete.");
        }
//        //Messaging init...
//        String accountSid = ctx.getEnvironment().getProperty("schematek.twilio.ACCOUNT_SID");
//        String authToken = ctx.getEnvironment().getProperty("schematek.twilio.AUTH_TOKEN");
//        Twilio.init(accountSid,authToken);

        log.info("main routine ended.");
    }
}
