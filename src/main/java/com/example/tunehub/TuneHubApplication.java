package com.example.tunehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TuneHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuneHubApplication.class, args);
    }

}
