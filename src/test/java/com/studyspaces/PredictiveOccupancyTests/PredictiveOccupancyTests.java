package com.studyspaces.PredictiveOccupancyTests;

import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.repository.*;
import com.studyspaces.spacefinder.service.PredictiveOccupancy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PredictiveOccupancyTests {

    @Mock
    private RealTimeOccupancyRepository realTimeRepo;

    @Mock
    private HistoricOccupancyRepository historicRepo;

    @InjectMocks
    private PredictiveOccupancy predictiveOccupancy;

    @BeforeEach
    public void setup() {
        // Initialize @Mock and @InjectMocks annotations
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTrainModelAndForecast() {
        String roomId = "room1";

        // Mock historic records for training
        List<OccupancyRecord> historicalRecords = new ArrayList<>();
        long now = System.currentTimeMillis();
        historicalRecords.add(new OccupancyRecord(now - 3600_000, Occupancy.LOW));
        historicalRecords.add(new OccupancyRecord(now, Occupancy.MEDIUM));

        RoomOccupancyRecord historicRoom = new RoomOccupancyRecord();
        historicRoom.setRecords(historicalRecords);

        when(historicRepo.findById(roomId)).thenReturn(Optional.of(historicRoom));

        // Train the model
        predictiveOccupancy.trainModel(roomId);

        // Mock recent records for forecasting
        List<OccupancyRecord> recentRecords = new ArrayList<>();
        recentRecords.add(new OccupancyRecord(now + 3600_000, Occupancy.HIGH));

        RoomOccupancyRecord recentRoom = new RoomOccupancyRecord();
        recentRoom.setRecords(recentRecords);

        when(realTimeRepo.findById(roomId)).thenReturn(Optional.of(recentRoom));

        // Forecast
        Map<Integer, Map<Integer, Double>> forecast = predictiveOccupancy.forecastNext7Days(roomId);

        // Assertions
        assertFalse(forecast.isEmpty());
        forecast.forEach((day, hours) -> {
            assertFalse(hours.isEmpty());
            hours.forEach((hour, value) -> assertTrue(value >= 0.0 && value <= 1.0));
        });

        // Verify interactions
        verify(historicRepo, times(1)).findById(roomId);
        verify(realTimeRepo, times(1)).findById(roomId);
    }
}