package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.model.*;

import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * OccupancyManager
 *
 *
 */

@Service
public class OccupancyManager {

    private final RealTimeOccupancyRepository repo;

    public OccupancyManager(RealTimeOccupancyRepository repo) {
        this.repo = repo;
    }

    // Retrieves latest occupancy level for a specified room
    public Occupancy getLastOccupancy(String RoomId) {
        return repo.findLastRoomOccupancy(RoomId)
                .map(OccupancyRecord::getOccupancyLevel)
                .orElse(null);
    }

    // Return time since last occupancy update for a room
    public long whenLastOccupancyWasAdded(String RoomId ) {
        OccupancyRecord lastOccupancy =
                repo.findLastRoomOccupancy(RoomId).isPresent()
                        ? repo.findLastRoomOccupancy(RoomId).get()
                        : null;

        if (lastOccupancy == null){
            return -1;
        }

        long lastTimestamp = lastOccupancy.getTimestamp();
        long now = Instant.now().getEpochSecond();

        return now - lastTimestamp;
    }

    public Boolean userCheckIn(String RoomId) {

        return false;
    }
}
