package com.studyspaces.spacefinder.dto;

public class BuildingUtilisationDTO {
    
    private String building;
    private double averageDemand;
    private double utilisationPercent;

    public BuildingUtilisationDTO(String building, double averageDemand){
        this.building = building;
        this.averageDemand = averageDemand;
        this.utilisationPercent = averageDemand * 100.0;
    }

    public String getBuilding(){
        return building;
    }

    public double getAverageDemand(){
        return averageDemand;
    }

    public double getUtilisationPercent(){
        return utilisationPercent;
    }
}
