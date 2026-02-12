package com.studyspaces.spacefinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "occupancy_records")
public class RoomOccupancyRecord {

    @Id
    private String id;

    private List<OccupancyRecord> records;

    public RoomOccupancyRecord() {}

    public RoomOccupancyRecord(String id, List<OccupancyRecord> records) {
        this.id = id;
        this.records = records;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OccupancyRecord> getRecords() {
        return records;
    }

    public void setRecords(List<OccupancyRecord> records) {
        this.records = records;
    }
}
