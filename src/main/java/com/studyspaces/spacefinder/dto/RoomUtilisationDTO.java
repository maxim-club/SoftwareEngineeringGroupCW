package com.studyspaces.spacefinder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomUtilisationDTO {
    
    private String roomId;
    private String roomLocation;
    private double averageDemand;
    private double utilisationPercent;
    private boolean underUtilised;
    private AnalyticsDataWarning warning;

    public RoomUtilisationDTO(
        String roomId,
        String roomLocation,
        double averageDemand,
        boolean underUtilised,
        AnalyticsDataWarning warning
    ) {
        this.roomId = roomId;
        this.roomLocation = roomLocation;
        this.averageDemand = averageDemand;
        this.utilisationPercent = averageDemand * 100.0;
        this.underUtilised = underUtilised;
        this.warning = warning;
    }

    public String getRoomId(){
        return roomId;
    }

    public String getRoomLocation(){
        return roomLocation;
    }

    public double getAverageDemand(){
        return averageDemand;
    }

    public double getUtilisationPercent(){
        return utilisationPercent;
    }

    public boolean isUnderUtilised() {
        return underUtilised;
    }

    public AnalyticsDataWarning getWarning(){
        return warning;
    }
}

