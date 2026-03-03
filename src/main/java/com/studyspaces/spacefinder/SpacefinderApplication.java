package com.studyspaces.spacefinder;

import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.model.UserRecord;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.studyspaces.spacefinder.*;

@EnableScheduling
@SpringBootApplication
public class SpacefinderApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpacefinderApplication.class, args);
    }
}
