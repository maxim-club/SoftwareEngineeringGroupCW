package com.studyspaces.spacefinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecord {
    @Id
    private String id;

    private String username;
    private String password;

    private List<StudySession> studySessionList = new ArrayList<>();
}
