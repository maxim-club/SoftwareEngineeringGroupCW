package com.studyspaces.spacefinder.dto;

public class PeakUsageDTO {
    
    private String roomId;
    private int peakHour;
    private double averageDemand;
    private AnalyticsDataWarning warning;

    public PeakUsageDTO(String roomId, int peakHour, double averageDemand, AnalyticsDataWarning warning){
        this.roomId = roomId;
        this.peakHour = peakHour;
        this.averageDemand = averageDemand;
        this.warning = warning;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getPeakHour(){
        return peakHour;
    }

    public double getAverageDemand(){
        return averageDemand;
    }

    public AnalyticsDataWarning getWarning() {
        return warning;
    }
}
