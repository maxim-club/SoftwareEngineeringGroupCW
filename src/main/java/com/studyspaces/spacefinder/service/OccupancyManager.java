package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.model.*;

import org.springframework.stereotype.Service;


@Service
public class OccupancyManager {

    private final RealTimeOccupancyRepository repo;

    public OccupancyManager(RealTimeOccupancyRepository repo) {
        this.repo = repo;
    }

    public Occupancy getLastOccupancy(String id) {
        OccupancyRecord lastOccupancy = repo.findLastRoomOccupancy(id).isPresent() ? repo.findLastRoomOccupancy(id).get() : null;

        if (lastOccupancy == null){
            return null;
        }

        return lastOccupancy.getOccupancyLevel();
    }
}
