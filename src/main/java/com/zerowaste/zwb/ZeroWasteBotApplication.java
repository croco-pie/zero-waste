package com.zerowaste.zwb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@Slf4j
@SpringBootApplication
@EnableAutoConfiguration
public class ZeroWasteBotApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ZeroWasteBotApplication.class).run(args);
        System.out.println(args);
        System.out.println("123-------123");
    }
}
