package com.studyspaces.spacefinder.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OccupancyRecord {

    private int timestamp;
    private Occupancy occupancyLevel;

    public OccupancyRecord() {}

}
