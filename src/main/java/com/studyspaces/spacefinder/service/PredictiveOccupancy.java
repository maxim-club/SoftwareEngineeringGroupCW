package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.OccupancyRecord;
import com.studyspaces.spacefinder.model.RoomOccupancyRecord;
import com.studyspaces.spacefinder.repository.HistoricOccupancyRepository;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import smile.data.DataFrame;
import smile.regression.*;
import smile.data.formula.Formula;


import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private RandomForest model;

    public PredictiveOccupancy(RealTimeOccupancyRepository repo, HistoricOccupancyRepository historicRepo){
        this.historicRepo = historicRepo;
        this.repo = repo;
    }

    public void trainModel(String roomId) throws SQLException {

        // 1. Fetch Historical records
        Optional<RoomOccupancyRecord> optionalRoom = historicRepo.findById(roomId);

        if(optionalRoom.isEmpty()){return;}

        RoomOccupancyRecord room = optionalRoom.get();

        if(room.getRecords() == null || room.getRecords().isEmpty()) return;

        List<OccupancyRecord> records = room.getRecords();

        long[] timestamps = records.stream()
                .mapToLong(OccupancyRecord::getTimestamp)
                .toArray();


        // 2. Map occupancy to a float value

        double[] y = records.stream()
                .map(OccupancyRecord::getOccupancyLevel)
                .mapToDouble(this::mapOccupancyToRatio)
                .toArray();


        // 3. Extract temporal features

        double[][] X = new double[timestamps.length][6];

        for (int i = 0; i < timestamps.length; i++) {

            LocalDateTime time = Instant.ofEpochMilli(timestamps[i]).atZone(ZoneId.systemDefault()).toLocalDateTime();

            int hour = time.getHour();
            int dayOfWeek = time.getDayOfWeek().getValue();

            // Cyclical Encoding
            double hourSin = Math.sin(2 * Math.PI * hour / 24.0);
            double hourCos = Math.cos(2 * Math.PI * hour / 24.0);

            double daySin = Math.sin(2 * Math.PI * dayOfWeek / 7.0);
            double dayCos = Math.cos(2 * Math.PI * dayOfWeek / 7.0);

            double isWeekend = (dayOfWeek >= 6) ? 1.0 : 0.0;

            X[i][0] = hourSin;
            X[i][1] = hourCos;
            X[i][2] = daySin;
            X[i][3] = dayCos;
            X[i][4] = isWeekend;
            X[i][5] = time.getMonthValue();
        }
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
