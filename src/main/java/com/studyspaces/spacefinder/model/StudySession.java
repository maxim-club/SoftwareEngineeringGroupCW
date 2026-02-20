package com.studyspaces.spacefinder.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class StudySession {
    public String roomId;
    public Long startTimestamp;
    public Long endTimestamp;


}
