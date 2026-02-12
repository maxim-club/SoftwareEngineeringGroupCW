package com.studyspaces.OccupancyManagerTests;

import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.OccupancyRecord;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.service.OccupancyManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
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

    // -------------------------
    // getLastOccupancy tests
    // -------------------------

    @Test
    void getLastOccupancy_returnsOccupancy_whenRecordExists() {
        String roomId = "room1";

        OccupancyRecord record = mock(OccupancyRecord.class);
        when(record.getOccupancyLevel()).thenReturn(Occupancy.BUSY);
        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.of(record));

        Occupancy result = occupancyManager.getLastOccupancy(roomId);

        assertEquals(Occupancy.BUSY, result);

        // Only ONE call now
        verify(repo, times(1)).findLastRoomOccupancy(roomId);
    }

    @Test
    void getLastOccupancy_returnsNull_whenNoRecordExists() {
        String roomId = "room1";

        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.empty());

        Occupancy result = occupancyManager.getLastOccupancy(roomId);

        assertNull(result);

        verify(repo, times(1)).findLastRoomOccupancy(roomId);
    }

    // -------------------------
    // whenLastOccupancyWasAdded tests
    // -------------------------

    @Test
    void whenLastOccupancyWasAdded_returnsMinusOne_whenNoRecordExists() {
        String roomId = "room1";

        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.empty());

        long result = occupancyManager.whenLastOccupancyWasAdded(roomId);

        assertEquals(-1L, result);

        // Called once (because .get() not executed)
        verify(repo, times(1)).findLastRoomOccupancy(roomId);
    }

    @Test
    void whenLastOccupancyWasAdded_returnsCorrectTimeDifference_whenRecordExists() {
        String roomId = "room1";

        long now = Instant.now().getEpochSecond();
        int fiveMinutesAgo = (int) (now - 300); // must be int

        OccupancyRecord record = mock(OccupancyRecord.class);
        when(record.getTimestamp()).thenReturn(fiveMinutesAgo);

        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.of(record));

        long result = occupancyManager.whenLastOccupancyWasAdded(roomId);

        assertTrue(result >= 300 && result <= 301);

        // STILL called twice in your current implementation
        verify(repo, times(2)).findLastRoomOccupancy(roomId);
    }
}
