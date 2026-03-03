package com.studyspaces.spacefinder.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OccupancyRecord {

    private long timestamp;
    private Occupancy occupancyLevel;

    public OccupancyRecord(long timestamp, Occupancy occupancyLevel) {
        this.timestamp = timestamp;
        this.occupancyLevel = occupancyLevel;
    }
}
