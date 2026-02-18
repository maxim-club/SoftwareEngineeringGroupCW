package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.dto.CheckInDTO;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.model.*;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

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

    // Converts DTO into model
    public CheckInReport toModel(CheckInDTO dto) {
        return new CheckInReport(
                dto.getClosed(),
                dto.getWifiIssue(),
                dto.getReserved(),
                dto.getFullyOccupied(),
                Occupancy.valueOf(dto.getOccupancy())
        );
    }

    // ===========================
    // Read Requests
    // ===========================

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

    // ===========================
    // Write Requests
    // ===========================

    // User checks in to a study space and submits report
    public boolean userCheckIn(String RoomId, CheckInReport report) {

        Optional<RoomOccupancyRecord> optionalRoom = repo.findById(RoomId);

        if(optionalRoom.isEmpty()) {
            return false;
        }

        RoomOccupancyRecord room = optionalRoom.get();

        // Create new occupancy record
        OccupancyRecord newRecord = new OccupancyRecord();
        newRecord.setOccupancyLevel(report.getOccupancy());
        newRecord.setTimestamp((int) (System.currentTimeMillis() / 1000));

        // Add record to document
        room.getRecords().add(newRecord);

        // Save updated document
        repo.save(room);

        return true;
    }

    // User Submits a report
    public void archiveOccupancyRecord(){

    }
}
