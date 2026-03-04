package com.studyspaces.spacefinder.model;

import lombok.Data;

@Data
public class ScheduleEntry {

    private String day;        // 1 = Monday, 7 = Sunday
    private String startTime;   // 0–23
    private String endTime;  // 0–23

    public ScheduleEntry() {}
}
