package com.studyspaces.spacefinder.model;

import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Amenities {


    private boolean desks;
    private boolean computers;
    private boolean foodAllowed;
    private boolean heaters;
    private boolean monitors;
    private boolean naturalLight;
    private boolean plugSockets;
    private boolean printers;
    private boolean projectors;
    private boolean silent;
    private boolean toiletNearby;
    private boolean waterFountainNearby;
    private boolean wheelchairAccessible;
    private boolean whiteboard;

    public Amenities() {}

    public List<Boolean> toList() {
        return List.of(plugSockets,
                desks,
                computers,
                printers,
                foodAllowed,
                waterFountainNearby,
                toiletNearby,
                wheelchairAccessible
        );
    }

}
