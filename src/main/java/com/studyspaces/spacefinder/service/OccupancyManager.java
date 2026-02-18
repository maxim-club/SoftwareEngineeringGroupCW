package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.dto.CheckInDTO;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.model.*;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    public Occupancy getLastOccupancy(String roomId) {
        return repo.findLastRoomOccupancy(roomId)
                .map(OccupancyRecord::getOccupancyLevel)
                .orElse(null);
    }

    // Return time since last occupancy update for a room
    public long whenLastOccupancyWasAdded(String roomId ) {
        Optional<OccupancyRecord> lastOccupancyOpt = repo.findLastRoomOccupancy(roomId);

        if (lastOccupancyOpt.isEmpty()) return -1;

        long lastTimestamp = lastOccupancyOpt.get().getTimestamp(); // in millis
        long now = System.currentTimeMillis();

        return (now - lastTimestamp) / 1000; // return seconds
    }

    // ===========================
    // Write Requests
    // ===========================

    // User checks in to a study space and submits report
    public boolean userCheckIn(String roomId, CheckInReport report) {

        if (roomId == null || report == null) {
            return false;
        }

        Optional<RoomOccupancyRecord> optionalRoom = repo.findById(roomId);

        if (optionalRoom.isEmpty()) {
            return false;
        }

        RoomOccupancyRecord room = optionalRoom.get();

        List<OccupancyRecord> records = room.getRecords();

        if (records == null) {
            records = new ArrayList<>();
            room.setRecords(records);
        }

        OccupancyRecord newRecord = new OccupancyRecord(
                System.currentTimeMillis(),
                report.getOccupancy(),
                report.getClosed(),
                report.getWifiIssue(),
                report.getReserved(),
                report.getFullyOccupied()
        );

        records.add(newRecord);
        repo.save(room);

        return true;
    }

    // If an OccupancyRecord > 7 days, archive record
    public void archiveOccupancyRecord(){

    }
}
