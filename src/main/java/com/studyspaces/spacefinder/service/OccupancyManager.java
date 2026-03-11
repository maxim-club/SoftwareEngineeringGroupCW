package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.dto.CheckInDTO;
import com.studyspaces.spacefinder.repository.HistoricOccupancyRepository;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.model.*;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


/**
 * OccupancyManager
 */

@Service
public class OccupancyManager {

    private final RealTimeOccupancyRepository repo;
    private final HistoricOccupancyRepository historicRepo;
    private final MongoTemplate mongoTemplate;

    public OccupancyManager(RealTimeOccupancyRepository repo, HistoricOccupancyRepository historicRepo, MongoTemplate mongoTemplate) {
        this.repo = repo;
        this.historicRepo = historicRepo;
        this.mongoTemplate = mongoTemplate;
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

    // Returns the average occupancy for a room from the last 7 days
    public Occupancy get7DayAverage(String roomId) {
        long sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000;

        Optional<RoomOccupancyRecord> optionalRoom = repo.findById(roomId);
        if (optionalRoom.isEmpty()) return null;

        RoomOccupancyRecord room = optionalRoom.get();

        if(room.getRecords() == null || room.getRecords().isEmpty()) return null;

        List<OccupancyRecord> recentRecords = room.getRecords().stream()
                .filter(r -> r.getTimestamp() > sevenDaysAgo)
                .toList();

        if (recentRecords.isEmpty()) return null;

        double average = recentRecords.stream()
                .mapToInt(r -> occupancyToValue((r.getOccupancyLevel())))
                .average().orElse(0);

        return valueToOccupancy(average);
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


    // ===========================
    // Data Transfers
    // ===========================

    // If an OccupancyRecord > 7 days, archive record. Runs daily
    @Scheduled(cron = "0 0 3 * * ?")
    public void archiveOccupancyRecord(){

        long sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000;
        List<RoomOccupancyRecord> rooms = repo.findRoomsWithRecordsOlderThan(sevenDaysAgo);

        // Find all room with old records
        for(RoomOccupancyRecord room : rooms){

            List<OccupancyRecord> oldRecords = room.getRecords().stream()
                    .filter(o -> o.getTimestamp() < sevenDaysAgo)
                    .toList();

            // Archive room
            RoomOccupancyRecord historicRoom = historicRepo.findById(room.getId())
                    .orElse(new RoomOccupancyRecord(room.getId(), new ArrayList<>()));

            historicRoom.getRecords().addAll(oldRecords);
            historicRepo.save(historicRoom);

            // Remove old records from real-time collection using MongoTemplate $pull
            Query query = new Query(Criteria.where("_id").is(room.getId()));
            Update update = new Update().pull("records", Query.query(Criteria.where("timestamp").lt(sevenDaysAgo)));
            mongoTemplate.updateFirst(query, update, RoomOccupancyRecord.class);

        }

    }


    // ===========================
    // Helper Functions
    // ===========================

    // Helper functions for get7DayAverage()
    private int occupancyToValue(Occupancy occupancy) {
        return switch (occupancy) {
            case EMPTY -> 0;
            case LOW -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
        };
    }
    private Occupancy valueToOccupancy(double value) {
        int rounded = (int) Math.round(value);

        return switch (rounded) {
            case 0 -> Occupancy.EMPTY;
            case 1 -> Occupancy.LOW;
            case 3 -> Occupancy.HIGH;
            default -> Occupancy.MEDIUM;
        };
    }
}
