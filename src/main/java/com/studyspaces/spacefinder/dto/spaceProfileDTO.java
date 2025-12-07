package com.studyspaces.spacefinder.dto;

import java.util.List;

public class spaceProfileDTO {
    private String name;
    private String building;
    private String floor;
    private String roomNumber;
    private int capacity;

    private List<String> photos;
    private String floorPlan;

    //accessibility
    private boolean wheelChairAccessible;
    private boolean stepFreeRoute;
    private boolean elevatorNearby;
    private boolean accessibleRestroom;
    private boolean adjustableFurniture;

    //nearby services
    private String nearestFoodOutlet;
    private String nearestRestroom;
    private String nearestWaterFountain;
    private String nearestBusStop;

    //getters + setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBuilding() {
        return building;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }
    public String getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public List<String> getPhotos() {
        return photos;
    }
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
    public String getFloorPlan() {
        return floorPlan;
    }
    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
    }
    public boolean isWheelChairAccessible() {
        return wheelChairAccessible;
    }
    public void setWheelChairAccessible(boolean wheelChairAccessible) {
        this.wheelChairAccessible = wheelChairAccessible;
    }
    public boolean isStepFreeRoute() {
        return stepFreeRoute;
    }
    public void setStepFreeRoute(boolean stepFreeRoute) {
        this.stepFreeRoute = stepFreeRoute;
    }
    public boolean isElevatorNearby() {
        return elevatorNearby;
    }
    public void setElevatorNearby(boolean elevatorNearby) {
        this.elevatorNearby = elevatorNearby;
    }
    public boolean isAccessibleRestroom() {
        return accessibleRestroom;
    }
    public void setAccessibleRestroom(boolean accessibleRestroom) {
        this.accessibleRestroom = accessibleRestroom;
    }
    public boolean isAdjustableFurniture() {
        return adjustableFurniture;
    }
    public void setAdjustableFurniture(boolean adjustableFurniture) {
        this.adjustableFurniture = adjustableFurniture;
    }
    public String getNearestFoodOutlet() {
        return nearestFoodOutlet;
    }
    public void setNearestFoodOutlet(String nearestFoodOutlet) {
        this.nearestFoodOutlet = nearestFoodOutlet;
    }
    public String getNearestRestroom() {
        return nearestRestroom;
    }
    public void setNearestRestroom(String nearestRestroom) {
        this.nearestRestroom = nearestRestroom;
    }
    public String getNearestWaterFountain() {
        return nearestWaterFountain;
    }
    public void setNearestWaterFountain(String nearestWaterFountain) {
        this.nearestWaterFountain = nearestWaterFountain;
    }
    public String getNearestBusStop() {
        return nearestBusStop;
    }
    public void setNearestBusStop(String nearestBusStop) {
        this.nearestBusStop = nearestBusStop;
    }

    
}
