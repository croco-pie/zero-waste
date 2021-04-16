package com.zerowaste.zwb;

import com.zerowaste.zwb.services.ZeroWasteBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
public class ZeroWasteBotApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ZeroWasteBotApplication.class);
        System.out.println("Service is starting...");

//        SpringApplication.run(ZeroWasteBotService.class, args);

        app.run(args).getBean(ZeroWasteBotService.class).printWaste();
        System.out.println("Service is stopping...");
        System.exit(0);
    }
}
