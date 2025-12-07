package com.studyspaces.spacefinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Simulated Data")
public class SimulatedData {

    @Id
    private String id;
    private String roomId; // links to RoomData id
    private String busynessLevel;

    // Constructors
    public SimulatedData() {}

    public SimulatedData(String id, String roomId, String busynessLevel) {
        this.id = id;
        this.roomId = roomId;
        this.busynessLevel = busynessLevel;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getBusynessLevel() { return busynessLevel; }
    public void setBusynessLevel(String busynessLevel) { this.busynessLevel = busynessLevel; }
}
