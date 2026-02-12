package com.studyspaces.spacefinder.model;

public class OccupancyRecord {

    private int timestamp;
    private Occupancy occupancyLevel;

    public OccupancyRecord() {}

    public OccupancyRecord(int timestamp, Occupancy occupancyLevel) {
        this.timestamp = timestamp;
        this.occupancyLevel = occupancyLevel;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Occupancy getOccupancyLevel() {
        return occupancyLevel;
    }

    public void setOccupancyLevel(Occupancy occupancyLevel) {
        this.occupancyLevel = occupancyLevel;
    }
}
