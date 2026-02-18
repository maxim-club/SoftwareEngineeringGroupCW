package com.studyspaces.OccupancyManagerTests;

import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.service.OccupancyManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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

        long nowMillis = System.currentTimeMillis();
        long fiveMinutesAgoMillis = nowMillis - 300_000;

        OccupancyRecord record = mock(OccupancyRecord.class);
        when(record.getTimestamp()).thenReturn(fiveMinutesAgoMillis);

        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.of(record));

        long result = occupancyManager.whenLastOccupancyWasAdded(roomId);

        assertTrue(result >= 300 && result <= 301);

        verify(repo, times(1)).findLastRoomOccupancy(roomId);

    }

    // -------------------------
    // userCheckIn tests
    // -------------------------

    @Test
    void userCheckIn_addsNewRecordAndSavesRoom() {

        String roomId = "room1";

        CheckInReport report = new CheckInReport(
                false,
                true,
                false,
                false,
                Occupancy.FREE
        );

        RoomOccupancyRecord room = new RoomOccupancyRecord();
        room.setRecords(new ArrayList<>());

        when(repo.findById(roomId)).thenReturn(Optional.of(room));

        boolean result = occupancyManager.userCheckIn(roomId, report);

        assertTrue(result);
        assertEquals(1, room.getRecords().size());

        OccupancyRecord savedRecord = room.getRecords().get(0);

        assertEquals(Occupancy.FREE, savedRecord.getOccupancyLevel());
        assertEquals(true, savedRecord.getWifiIssue());
        assertEquals(false, savedRecord.getClosed());

        verify(repo).save(room);
    }

}
