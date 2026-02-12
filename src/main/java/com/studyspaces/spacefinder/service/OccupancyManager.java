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

    public void getCurrentOccupancy(String id) {
        RoomOccupancyRecord room = repo.findById(id).isPresent() ? repo.findById(id).get() : null;

		return;	
    }
}
