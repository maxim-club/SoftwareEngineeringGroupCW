package com.studyspaces.spacefinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.studyspaces.spacefinder.model.OccupancyRecordEntry;

import java.util.List;

@Document(collection = "occupancy_records")
public class OccupancyRecord {

    @Id
    private String id;

    private List<OccupancyRecordEntry> records;

    public OccupancyRecord() {}

    public OccupancyRecord(String id, List<OccupancyRecordEntry> records) {
        this.id = id;
        this.records = records;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<OccupancyRecordEntry> getRecords() {
        return records;
    }

    public void setRecords(List<OccupancyRecordEntry> records) {
        this.records = records;
    }
}
