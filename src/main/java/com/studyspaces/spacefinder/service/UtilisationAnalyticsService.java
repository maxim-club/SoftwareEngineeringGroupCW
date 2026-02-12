package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.dto.RoomUtilisationDTO;
import com.studyspaces.spacefinder.dto.PeakUsageDTO;
import com.studyspaces.spacefinder.dto.AnalyticsDataWarning;
import com.studyspaces.spacefinder.dto.BuildingUtilisationDTO;
import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.RoomOccupancyRecord;
import com.studyspaces.spacefinder.model.OccupancyRecord;
import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.repository.HistoricOccupancyRepository;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UtilisationAnalyticsService {
    private final StudySpaceRepository studySpaceRepo;
    private final HistoricOccupancyRepository historicRepo;

    public UtilisationAnalyticsService(
        StudySpaceRepository studySpaceRepo,
        HistoricOccupancyRepository historicRepo
    ) {
        this.studySpaceRepo = studySpaceRepo;
        this.historicRepo = historicRepo;
    }

    //average demand per room and detects if a room is underutilised
    //data warnings: no records -> insufficient data, less than 5 records -> insufficient data, DB error -> data source offline, enough data -> none
    public List<RoomUtilisationDTO> getRoomUtilisationSummary() {

        List<RoomUtilisationDTO> results = new ArrayList<>();

        List<StudySpaceProfile> rooms = studySpaceRepo.findAll();

        for (StudySpaceProfile room : rooms) {

            try {
                Optional<RoomOccupancyRecord> recordOpt = historicRepo.findById(room.getId());

                if (recordOpt.isEmpty() || recordOpt.get().getRecords() == null || recordOpt.get().getRecords().isEmpty()){
                    results.add(new RoomUtilisationDTO(room.getId(), room.getRoomLocation(), 0.0, true, AnalyticsDataWarning.INSUFFICIENT_DATA));
                    continue;
                }
                
                List<OccupancyRecord> records = recordOpt.get().getRecords();

                AnalyticsDataWarning warning = AnalyticsDataWarning.NONE;

                if (records.size() < 5){
                    warning = AnalyticsDataWarning.INSUFFICIENT_DATA;
                }
                
                double avgDemand = computeAverageDemand(records);
                boolean underUtilised = avgDemand < 0.30;

                results.add(new RoomUtilisationDTO(room.getId(), room.getRoomLocation(), avgDemand, underUtilised, warning));
            } catch (Exception e) {
                results.add(new RoomUtilisationDTO(room.getId(), room.getRoomLocation(), 0.0, true, AnalyticsDataWarning.DATA_SOURCE_OFFLINE));
            }

        }
        return results;
    }

    private double computeAverageDemand(List<OccupancyRecord> entries){
        return entries.stream().mapToDouble(e -> mapOccupancyToRatio(e.getOccupancyLevel())).average().orElse(0.0);
    }

    //maps occupancy to utilisation ratio as occupancy is currently categorical:empty, free, moderate, busy
    private double mapOccupancyToRatio(Occupancy level){

        if (level == null) return 0.0;

        switch (level) {
            case EMPTY:
                return 0.0;
            case FREE:
                return 0.25;
            case MODERATE:
                return 0.5;
            case BUSY:
                return 0.85;
            default:
                return 0.0;
        }
    }

    //for a room it converts each timestamp into hour and then groups demand by hour.
    //it then calculates the average demand per hour and returns the hour with the highest average demand

    public PeakUsageDTO getPeakUsageForRoom(String roomId){

        try{

            Optional<RoomOccupancyRecord> recordOpt = historicRepo.findById(roomId);

            if (recordOpt.isEmpty() || recordOpt.get().getRecords() == null || recordOpt.get().getRecords().isEmpty()) {
                return new PeakUsageDTO(roomId, -1, 0.0, AnalyticsDataWarning.INSUFFICIENT_DATA);
            }

            List<OccupancyRecord> records = recordOpt.get().getRecords();

            if (records.size() < 5) {
                return new PeakUsageDTO(roomId, -1, 0.0, AnalyticsDataWarning.INSUFFICIENT_DATA);
            }

            Map<Integer, List<Double>> hourlyDemand = new HashMap<>();

            for (OccupancyRecord entry : records){

                int hour = extractHour(entry.getTimestamp());
                double demand = mapOccupancyToRatio(entry.getOccupancyLevel());

                hourlyDemand.computeIfAbsent(hour, k -> new ArrayList<>()).add(demand);
            }

            int peakHour = -1;
            double highestAvg = 0.0;

            for (Map.Entry<Integer, List<Double>> e : hourlyDemand.entrySet()){

                double avg = e.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);

                if (avg > highestAvg) {
                    highestAvg = avg;
                    peakHour = e.getKey();
                }
            }

            return new PeakUsageDTO(roomId, peakHour, highestAvg, AnalyticsDataWarning.NONE);
        } catch (Exception e) {
            return new PeakUsageDTO(roomId, -1, 0.0, AnalyticsDataWarning.DATA_SOURCE_OFFLINE);
        }
    }

    //helper function to extract the hour
    private int extractHour(int timestamp){
        Instant instant = Instant.ofEpochSecond(timestamp);
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());

        return dateTime.getHour(); //0-23
    }

    public List<BuildingUtilisationDTO> getBuildingUtilisationSummary(){

        List<StudySpaceProfile> rooms = studySpaceRepo.findAll();
        List<BuildingUtilisationDTO> results = new ArrayList<>();

        //maps each building to average demand values
        Map<String, List<Double>> buildingDemandMap = new HashMap<>();

        for (StudySpaceProfile room : rooms){

            Optional<RoomOccupancyRecord> recordOpt = historicRepo.findById(room.getId());

            if (recordOpt.isEmpty() || recordOpt.get().getRecords() == null || recordOpt.get().getRecords().isEmpty()){
                continue; // will add warning logic later
            }

            double avgDemand = computeAverageDemand(recordOpt.get().getRecords());

            buildingDemandMap.computeIfAbsent(room.getRoomLocation(), k -> new ArrayList<>()).add(avgDemand);
        }

        //calculate building averages
        for (Map.Entry<String, List<Double>> entry : buildingDemandMap.entrySet()){
            
            double buildingAvg = entry.getValue().stream().mapToDouble(d -> d).average().orElse(0.0);

            results.add(new BuildingUtilisationDTO(entry.getKey(), buildingAvg));
        }

        //returns the most used to least used buildings
        results.sort((a, b) -> Double.compare(b.getAverageDemand(), a.getAverageDemand()));
        return results;
    }

    //returns most used rooms
    public List<RoomUtilisationDTO> getMostUsedRooms() {

        List<RoomUtilisationDTO> rooms = getRoomUtilisationSummary();
        rooms.sort((a,b) -> Double.compare(b.getAverageDemand(), a.getAverageDemand()));
        return rooms;
    }

    //returns least used rooms
    public List<RoomUtilisationDTO> getLeastUsedRooms() {

        List<RoomUtilisationDTO> rooms = getRoomUtilisationSummary();
        rooms.sort((a,b) -> Double.compare(a.getAverageDemand(), b.getAverageDemand()));
        return rooms;
    }

    //to return only underutilised rooms which was previously defined as used <30%
    public List<RoomUtilisationDTO> getUnderUtilisedRooms(){
        return getRoomUtilisationSummary().stream().filter(RoomUtilisationDTO::isUnderUtilised).toList();
    }
}
