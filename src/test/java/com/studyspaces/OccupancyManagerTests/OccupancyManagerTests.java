package com.studyspaces.OccupancyManagerTests;

import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import com.studyspaces.spacefinder.repository.HistoricOccupancyRepository;
import com.studyspaces.spacefinder.service.OccupancyManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class OccupancyManagerTests {

    @Mock
    private RealTimeOccupancyRepository repo;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private HistoricOccupancyRepository historicRepo;

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
        when(record.getOccupancyLevel()).thenReturn(Occupancy.HIGH);
        when(repo.findLastRoomOccupancy(roomId))
                .thenReturn(Optional.of(record));

        Occupancy result = occupancyManager.getLastOccupancy(roomId);

        assertEquals(Occupancy.HIGH, result);

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
                Occupancy.LOW
        );

        RoomOccupancyRecord room = new RoomOccupancyRecord();
        room.setRecords(new ArrayList<>());

        when(repo.findById(roomId)).thenReturn(Optional.of(room));

        boolean result = occupancyManager.userCheckIn(roomId, report);

        assertTrue(result);
        assertEquals(1, room.getRecords().size());

        OccupancyRecord savedRecord = room.getRecords().get(0);

        assertEquals(Occupancy.LOW, savedRecord.getOccupancyLevel());
        assertEquals(true, savedRecord.getWifiIssue());
        assertEquals(false, savedRecord.getClosed());

        verify(repo).save(room);
    }

    @Test
    void userCheckIn_nulLReport(){
        String roomId = "room1";
        CheckInReport report = null;

        RoomOccupancyRecord room = new RoomOccupancyRecord();
        room.setRecords(new ArrayList<>());

        when(repo.findById(roomId)).thenReturn(Optional.of(room));
        boolean result = occupancyManager.userCheckIn(roomId, report);

        assertFalse(result);
    }

    @Test
    void userCheckIn_nulLRoom(){
        String roomId = null;
        CheckInReport report = new CheckInReport(
                false,
                true,
                false,
                false,
                Occupancy.LOW
        );

        RoomOccupancyRecord room = new RoomOccupancyRecord();
        room.setRecords(new ArrayList<>());

        boolean result = occupancyManager.userCheckIn(roomId, report);
        assertFalse(result);
    }

    // -------------------------
    // get7DayAverage tests
    // -------------------------

    @Test
    void get7DayAverage_returnsNull_whenRoomDoesNotExist() {
        String roomId = "room1";

        when(repo.findById(roomId)).thenReturn(Optional.empty());

        Occupancy result = occupancyManager.get7DayAverage(roomId);

        assertNull(result);

        verify(repo, times(1)).findById(roomId);
    }

    @Test
    void get7DayAverage_returnsNull_whenNoRecordsExist() {
        String roomId = "room1";

        RoomOccupancyRecord room = new RoomOccupancyRecord();
        room.setRecords(new ArrayList<>());

        when(repo.findById(roomId)).thenReturn(Optional.of(room));

        Occupancy result = occupancyManager.get7DayAverage(roomId);

        assertNull(result);

        verify(repo, times(1)).findById(roomId);
    }

    @Test
    void get7DayAverage_returnsNull_whenNoRecordsInLast7Days() {
        String roomId = "room1";

        long oldTimestamp = System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000;

        OccupancyRecord oldRecord = mock(OccupancyRecord.class);
        when(oldRecord.getTimestamp()).thenReturn(oldTimestamp);
        when(oldRecord.getOccupancyLevel()).thenReturn(Occupancy.HIGH);

        RoomOccupancyRecord room = new RoomOccupancyRecord();
        room.setRecords(List.of(oldRecord));

        when(repo.findById(roomId)).thenReturn(Optional.of(room));

        Occupancy result = occupancyManager.get7DayAverage(roomId);

        assertNull(result);

        verify(repo, times(1)).findById(roomId);
    }

    @Test
    void get7DayAverage_returnsCorrectAverageOccupancy() {
        String roomId = "room1";

        long now = System.currentTimeMillis();
        long twoDaysAgo = now - 2L * 24 * 60 * 60 * 1000;
        long fiveDaysAgo = now - 5L * 24 * 60 * 60 * 1000;

        OccupancyRecord record1 = mock(OccupancyRecord.class);
        when(record1.getTimestamp()).thenReturn(twoDaysAgo);
        when(record1.getOccupancyLevel()).thenReturn(Occupancy.LOW);

        OccupancyRecord record2 = mock(OccupancyRecord.class);
        when(record2.getTimestamp()).thenReturn(fiveDaysAgo);
        when(record2.getOccupancyLevel()).thenReturn(Occupancy.HIGH);

        RoomOccupancyRecord room = new RoomOccupancyRecord();
        room.setRecords(List.of(record1, record2));

        when(repo.findById(roomId)).thenReturn(Optional.of(room));

        Occupancy result = occupancyManager.get7DayAverage(roomId);

        // FREE (1) + BUSY (3) -> avg = 2 -> MODERATE
        assertEquals(Occupancy.MEDIUM, result);

        verify(repo, times(1)).findById(roomId);
    }


    // -------------------------
    // archiveOccupancyRecord tests
    // -------------------------

    @Test
    void archiveOccupancyRecord_archivesOldRecordsAndRemovesThem() {

        String roomId = "room1";

        long now = System.currentTimeMillis();
        long eightDaysAgo = now - (8L * 24 * 60 * 60 * 1000);
        long oneDayAgo = now - ((long) 24 * 60 * 60 * 1000);

        // Mock records
        OccupancyRecord oldRecord = mock(OccupancyRecord.class);
        when(oldRecord.getTimestamp()).thenReturn(eightDaysAgo);

        OccupancyRecord recentRecord = mock(OccupancyRecord.class);
        when(recentRecord.getTimestamp()).thenReturn(oneDayAgo);

        ArrayList<OccupancyRecord> records = new ArrayList<>();
        records.add(oldRecord);
        records.add(recentRecord);

        RoomOccupancyRecord room = new RoomOccupancyRecord(roomId, records);

        when(repo.findRoomsWithRecordsOlderThan(anyLong()))
                .thenReturn(List.of(room));

        when(historicRepo.findById(roomId))
                .thenReturn(Optional.empty());

        // Act
        occupancyManager.archiveOccupancyRecord();

        // Verify historic save
        ArgumentCaptor<RoomOccupancyRecord> captor =
                ArgumentCaptor.forClass(RoomOccupancyRecord.class);

        verify(historicRepo, times(1)).save(captor.capture());

        RoomOccupancyRecord savedHistoricRoom = captor.getValue();

        assertEquals(roomId, savedHistoricRoom.getId());
        assertEquals(1, savedHistoricRoom.getRecords().size());
        assertEquals(oldRecord, savedHistoricRoom.getRecords().get(0));

        // Verify Mongo pull was executed
        verify(mongoTemplate, times(1))
                .updateFirst(any(Query.class), any(Update.class), eq(RoomOccupancyRecord.class));
    }
}
