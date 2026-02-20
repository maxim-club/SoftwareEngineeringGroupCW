package com.studyspaces.spacefinder.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Document(collection = "occupancy_records")
public class RoomOccupancyRecord {

    @Id
    private String id;

    private List<OccupancyRecord> records;

    public RoomOccupancyRecord(String id, List<OccupancyRecord> records) {
        this.id = id;
        this.records = records;
    }

}
