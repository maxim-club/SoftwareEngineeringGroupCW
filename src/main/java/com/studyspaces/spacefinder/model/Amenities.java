package com.studyspaces.spacefinder.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class Amenities {

    private boolean plugSockets;
    private boolean desks;
    private boolean computers;
    private boolean printers;
    private boolean foodAllowed;
    private boolean waterFountainNearby;
    private boolean toiletNearby;
    private boolean wheelchairAccessible;

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
