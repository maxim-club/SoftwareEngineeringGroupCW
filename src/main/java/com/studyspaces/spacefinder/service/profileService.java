package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.dto.spaceProfileDTO;
import org.springframework.stereotype.Service;

@Service
public class profileService {
    public spaceProfileDTO getSpaceProfile(Long spaceID) {

        spaceProfileDTO profile = new spaceProfileDTO();

        //placeholder values
        profile.setName("Library Study Room");
        profile.setBuilding("Library");
        profile.setFloor("4");
        profile.setRoomNumber("4.14");
        profile.setCapacity(40);

        profile.setWheelChairAccessible(true);
        profile.setStepFreeRoute(true);
        profile.setElevatorNearby(true);

        profile.setNearestFoodOutlet("Lime tree");
        profile.setNearestRestroom("First Floor");

        return profile;
    }
}
