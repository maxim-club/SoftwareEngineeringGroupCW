package com.studyspaces.spacefinder.service;

import java.util.ArrayList;
import java.util.List;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.model.*;

public class OccupancyManager {

    private final RealTimeOccupancyRepository repo;

    public OccupancyManager(RealTimeOccupancyRepository repo) {
        this.repo = repo;
    }

    public void getCurrentOccupancy() {

    }
}
