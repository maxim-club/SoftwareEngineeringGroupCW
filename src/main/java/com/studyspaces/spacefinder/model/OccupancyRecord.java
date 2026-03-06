package com.studyspaces.spacefinder.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@NoArgsConstructor
public class OccupancyRecord {

    private long timestamp;
    private Occupancy occupancyLevel;
    private Boolean closed;
    private Boolean wifiIssue;
    private Boolean reserved;
    private Boolean fullyOccupied;


    public OccupancyRecord(long timestamp, Occupancy occupancyLevel, Boolean closed, Boolean wifiIssue, Boolean reserved, Boolean fullyOccupied) {
        this.timestamp = timestamp;
        this.occupancyLevel = occupancyLevel;
        this.closed = closed;
        this.wifiIssue = wifiIssue;
        this.reserved = reserved;
        this.fullyOccupied = fullyOccupied;
    }

    public OccupancyRecord(long timestamp, Occupancy occupancyLevel) {
        this.timestamp = timestamp;
        this.occupancyLevel = occupancyLevel;
    }
}
