package com.studyspaces.spacefinder.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OccupancyGraphPointDTO {
    
    private String day;  // will be used for weekly graphs
    private Integer hour;  // will be used for hourly graphs
    private double averageOccupancy;

    public OccupancyGraphPointDTO(String day, Integer hour, double averageOccupancy){
        this.day = day;
        this.hour = hour;
        this.averageOccupancy = averageOccupancy;
    }

    public String getDay(){
        return day;
    }

    public Integer getHour(){
        return hour;
    }

    @JsonProperty("Average Occupancy")
    public double getAverageOccupancy(){
        return averageOccupancy;
    }
}


