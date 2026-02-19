package com.studyspaces.spacefinder.dto;

import java.util.List;

public class PeakUsageDTO {
    
    private String roomId;
    private List<Integer> busiestTimes;
    private List<String> busiestDays;
    private AnalyticsDataWarning warning;

    public PeakUsageDTO(String roomId, List<Integer> busiestTimes, List<String> busiestDays, AnalyticsDataWarning warning){
        this.roomId = roomId;
        this.busiestTimes = busiestTimes;
        this.busiestDays = busiestDays;
        this.warning = warning;
    }

    public String getRoomId() {
        return roomId;
    }

    public List<Integer> getBusiestTimes(){
        return busiestTimes;
    }

    public List<String> getBusiestDays(){
        return busiestDays;
    }

    public AnalyticsDataWarning getWarning() {
        return warning;
    }
}
