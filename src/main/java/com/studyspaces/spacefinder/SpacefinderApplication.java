package com.studyspaces.spacefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpacefinderApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpacefinderApplication.class, args);
    }

}
