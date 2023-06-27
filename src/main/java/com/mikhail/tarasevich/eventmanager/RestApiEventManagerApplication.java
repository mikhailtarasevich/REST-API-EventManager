package com.mikhail.tarasevich.eventmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mikhail.tarasevich.eventmanager")
public class RestApiEventManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiEventManagerApplication.class, args);
    }

}
