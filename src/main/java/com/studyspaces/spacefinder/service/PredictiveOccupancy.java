package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.OccupancyRecord;
import com.studyspaces.spacefinder.model.RoomOccupancyRecord;
import com.studyspaces.spacefinder.repository.HistoricOccupancyRepository;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import smile.data.DataFrame;
import smile.data.vector.*;
import smile.regression.*;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map;

/**
 * PredictiveOccupancy
 */

@Service
public class PredictiveOccupancy {

    private final HistoricOccupancyRepository historicRepo;
    private final RealTimeOccupancyRepository repo;

    public PredictiveOccupancy(RealTimeOccupancyRepository repo, HistoricOccupancyRepository historicRepo){
        this.historicRepo = historicRepo;
        this.repo = repo;
    }

    public void trainModel(String roomId){

        // 1. Fetch Historical records
        Optional<RoomOccupancyRecord> optionalRoom = historicRepo.findById(roomId);

        if(optionalRoom.isEmpty()){return;}

        RoomOccupancyRecord room = optionalRoom.get();

        if(room.getRecords() == null || room.getRecords().isEmpty()) return;

        List<OccupancyRecord> records = room.getRecords();

        long[] timestamps = records.stream()
                .mapToLong(OccupancyRecord::getTimestamp)
                .toArray();

        double[] y = records.stream()
                .map(OccupancyRecord::getOccupancyLevel)
                .mapToDouble(this::mapOccupancyToRatio)
                .toArray();

        // 2. Map occupancy to a float value
        // 3. Extract temporal features
        // 4. Train ML mode
    }

    public Map<Integer, Map<Integer, Double>> forecastNext7Days(String roomId){
        return null;
    }


    private double mapOccupancyToRatio(Occupancy occupancy){
        return switch (occupancy) {
            case EMPTY -> 0.0;
            case LOW -> 0.25;
            case MEDIUM -> 0.5;
            case HIGH -> 0.85;
        };
    }
}
