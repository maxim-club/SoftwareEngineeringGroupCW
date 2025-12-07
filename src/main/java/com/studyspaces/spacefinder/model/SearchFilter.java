package com.studyspaces.spacefinder.model;

public class SearchFilter {

    private String id; // from Room Data
    private String location;
    private Boolean hasWifi;
    private Boolean hasOutlets;
    private Boolean hasGroupSeating;
    private Boolean isQuiet;
    private String busynessLevel; // from Simulated Data

    // Constructors
    public SearchFilter() {}

    public SearchFilter(String id, String location, Boolean hasWifi, Boolean hasOutlets, Boolean hasGroupSeating, Boolean isQuiet, String busynessLevel) {
        this.id = id;
        this.location = location;
        this.hasWifi = hasWifi;
        this.hasOutlets = hasOutlets;
        this.hasGroupSeating = hasGroupSeating;
        this.isQuiet = isQuiet;
        this.busynessLevel = busynessLevel;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Boolean getHasWifi() { return hasWifi; }
    public void setHasWifi(Boolean hasWifi) { this.hasWifi = hasWifi; }

    public Boolean getHasOutlets() { return hasOutlets; }
    public void setHasOutlets(Boolean hasOutlets) { this.hasOutlets = hasOutlets; }

    public Boolean getHasGroupSeating() { return hasGroupSeating; }
    public void setHasGroupSeating(Boolean hasGroupSeating) { this.hasGroupSeating = hasGroupSeating; }

    public Boolean getIsQuiet() { return isQuiet; }
    public void setIsQuiet(Boolean isQuiet) { this.isQuiet = isQuiet; }

    public String getBusynessLevel() { return busynessLevel; }
    public void setBusynessLevel(String busynessLevel) { this.busynessLevel = busynessLevel; }
}
