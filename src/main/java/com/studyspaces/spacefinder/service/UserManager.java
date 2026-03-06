package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.model.StudySession;
import com.studyspaces.spacefinder.model.UserRecord;
import com.studyspaces.spacefinder.repository.UserRepository;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class UserManager {
    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // check login info and return result
    public Boolean checkLogin(String username, String password){
        UserRecord user = userRepository.findUserRecordByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        return user.getPassword().equals(password);
    }

    //starts a new study session for user
    public void startStudySession(String username, String roomId){
        UserRecord user = userRepository.findUserRecordByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        StudySession session = new StudySession();
        session.setRoomId(roomId);
        session.setStartTimestamp(System.currentTimeMillis()); //Timestamp in millis
        session.setEndTimestamp(null);


        user.getStudySessionList().add(session);
        userRepository.save(user);

    }


    //ends the most latest session by saving the end timestamp
    public void endStudySession(String username){
        UserRecord user = userRepository.findUserRecordByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));

        List<StudySession> sessionList = user.getStudySessionList();
        StudySession session = sessionList.get(sessionList.size() - 1);

        session.setEndTimestamp(System.currentTimeMillis()); //Timestamp in millis

        userRepository.save(user);

    }

    public boolean signUp(Map<String, String> signupData){
        String username = signupData.get("username");
        String password = signupData.get("password"); // You should hash this in real apps
        String email = signupData.get("email");

        // Check if user already exists
        if (userRepository.findUserRecordByUsername(username).isPresent()) {
            return false;
        }

        // Create a new UserRecord
        UserRecord newUser = new UserRecord();
        newUser.setUsername(username);
        newUser.setPassword(password); // Hash in production!
        newUser.setEmail(email);

        // Save to DB
        userRepository.save(newUser);
        return true;
    }

}
