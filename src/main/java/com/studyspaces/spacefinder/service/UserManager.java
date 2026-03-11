package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.model.StudySession;
import com.studyspaces.spacefinder.model.UserRecord;
import com.studyspaces.spacefinder.repository.UserRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

}
