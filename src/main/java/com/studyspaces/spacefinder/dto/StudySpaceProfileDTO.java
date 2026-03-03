package com.studyspaces.spacefinder.dto;

import java.util.List;

import com.studyspaces.spacefinder.model.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudySpaceProfileDTO {

    private String id;

    // Core info
    private String roomLocation;
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
}