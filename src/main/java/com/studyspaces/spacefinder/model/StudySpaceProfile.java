package com.studyspaces.spacefinder.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import com.studyspaces.spacefinder.model.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@Document(collection = "Room Data") //this is the name of the collection in the database
public class StudySpaceProfile {

    @Id
    private String id;

    // Core info
    private String roomLocation; // This will be room and building combined into one.
    private String notes;

    // Status
    private Occupancy occupancy;
    private NoiseLevel noiseLevel;

    // Groups
    private boolean suitableForGroups;
    private Integer maxGroupSize;

    // Facilities
    private Amenities amenities;

    // Time + place
    private List<ScheduleEntry> schedule;
    private Coordinates coordinates;

    public StudySpaceProfile() {}

    public FilterQuery toFilterQuery(){
        return new FilterQuery(noiseLevel, occupancy, amenities, suitableForGroups, maxGroupSize);
    }
}
