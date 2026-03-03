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

    // Integration test for mongoDB with the repositories
    @Bean
    CommandLineRunner testRepositories(
            StudySpaceRepository studySpaceRepository,
            RealTimeOccupancyRepository occupancyRepository,
            UserRepository userRepository
    ) {
        return args -> {

            System.out.println("=== Testing Mongo Repositories ===");
            studySpaceRepository.findAll()
                    .forEach(s -> System.out.println(s.getId()));

            // Test StudySpace insert
            StudySpaceProfile space = new StudySpaceProfile();
            space.setId("test-room");
            space.setRoomLocation("Test Building - Room 101");

            studySpaceRepository.save(space);

            // Test fetch
            var found = studySpaceRepository.findById("test-room");
            System.out.println("Found space: " + found);

            // Test User insert
            UserRecord user = new UserRecord();
            user.setId("test-user");
            user.setUsername("demo");
            user.setPassword("hashed-password");

            userRepository.save(user);

            System.out.println("Users count: " + userRepository.count());

            // Test occupancy lookup
            boolean exists =
                    occupancyRepository.existsById("test-room");

            System.out.println("Occupancy exists: " + exists);

            System.out.println("=== Repository test complete ===");
        };
    }
}
