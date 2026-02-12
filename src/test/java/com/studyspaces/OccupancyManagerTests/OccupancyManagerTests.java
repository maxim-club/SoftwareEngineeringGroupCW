package com.studyspaces.OccupancyManagerTests;

import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.service.OccupancyManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OccupancyManagerTests {
    @Mock
    private RealTimeOccupancyRepository repo;

    @InjectMocks
    private OccupancyManager occupancyManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLastOccupancy_returnsOccupancy_whenRecordExists() {
        // Arrange
        String roomId = "room1";
        Occupancy expectedOccupancy = Occupancy.BUSY; // adjust if needed

        OccupancyRecord mockRecord = mock(OccupancyRecord.class);
        when(mockRecord.getOccupancyLevel()).thenReturn(expectedOccupancy);

        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.of(mockRecord));

        // Act
        Occupancy result = occupancyManager.getLastOccupancy(roomId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOccupancy, result);
        verify(repo, times(2)).findLastRoomOccupancy(roomId);
    }

    @Test
    void getLastOccupancy_returnsNull_whenNoRecordExists() {
        // Arrange
        String roomId = "room1";
        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.empty());

        // Act
        Occupancy result = occupancyManager.getLastOccupancy(roomId);

        // Assert
        assertNull(result);
        verify(repo, times(1)).findLastRoomOccupancy(roomId);
    }
}
