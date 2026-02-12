package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.model.*;

import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class OccupancyManager {

    private final RealTimeOccupancyRepository repo;

    public OccupancyManager(RealTimeOccupancyRepository repo) {
        this.repo = repo;
    }

    // Retrieves latest occupancy level for a specified room
    public Occupancy getLastOccupancy(String Roomid) {
        return repo.findLastRoomOccupancy(Roomid)
                .map(OccupancyRecord::getOccupancyLevel)
                .orElse(null);
    }

    public long whenLastOccupancyWasAdded(String Roomid ) {
        OccupancyRecord lastOccupancy =
                repo.findLastRoomOccupancy(Roomid).isPresent()
                        ? repo.findLastRoomOccupancy(Roomid).get()
                        : null;

        if (lastOccupancy == null){
            return -1;
        }

        long lastTimestamp = lastOccupancy.getTimestamp();
        long now = Instant.now().getEpochSecond();

        return now - lastTimestamp;
    }
}
