package com.studyspaces.UtilisationAnalyticsTests;

import com.studyspaces.spacefinder.service.*;
import com.studyspaces.spacefinder.dto.*;
import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.repository.HistoricOccupancyRepository;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtilisationAnalyticsServiceTest {

    @Mock
    private StudySpaceRepository studySpaceRepo;

    @Mock
    private HistoricOccupancyRepository historicRepo;

    @InjectMocks
    private UtilisationAnalyticsService service;

    private StudySpaceProfile room;

    @BeforeEach
    void setup() {
        room = new StudySpaceProfile();
        room.setId("room1");
        room.setRoomLocation("Library");
    }

    //helper method
    private OccupancyRecord record(Occupancy level, int hour, int dayOfWeek) {

        OccupancyRecord r = new OccupancyRecord();
        r.setOccupancyLevel(level);

        ZonedDateTime time = ZonedDateTime.now()
                .withHour(hour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .with(java.time.DayOfWeek.of(dayOfWeek));

        r.setTimestamp(time.toInstant().toEpochMilli());

        return r;
    }

    private OccupancyRecord record(Occupancy level) {
        OccupancyRecord r = new OccupancyRecord();
        r.setOccupancyLevel(level);
        r.setTimestamp(Instant.now().toEpochMilli());
        return r;
    }

    private RoomOccupancyRecord createRecord(List<OccupancyRecord> records) {
        RoomOccupancyRecord rec = new RoomOccupancyRecord();
        rec.setRecords(records);
        return rec;
    }

    // ------------------------------------------------
    // BLACK BOX TESTS
    // ------------------------------------------------

    @Test
    void getRoomUtilisationSummary_noRecords_returnsInsufficientData() {

        when(studySpaceRepo.findAll()).thenReturn(List.of(room));
        when(historicRepo.findById("room1")).thenReturn(Optional.empty());

        List<RoomUtilisationDTO> result = service.getRoomUtilisationSummary();

        assertEquals(1, result.size());
        assertEquals(AnalyticsDataWarning.INSUFFICIENT_DATA, result.get(0).getWarning());
        assertTrue(result.get(0).isUnderUtilised());
    }

    @Test
    void getRoomUtilisationSummary_lowDemand_detectsUnderUtilised() {

        List<OccupancyRecord> records = List.of(
                record(Occupancy.EMPTY),
                record(Occupancy.LOW),
                record(Occupancy.EMPTY),
                record(Occupancy.LOW),
                record(Occupancy.EMPTY)
        );

        when(studySpaceRepo.findAll()).thenReturn(List.of(room));
        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));

        List<RoomUtilisationDTO> result = service.getRoomUtilisationSummary();

        assertTrue(result.get(0).isUnderUtilised());
    }

    @Test
    void getMostUsedRooms_sortedDescending() {

        StudySpaceProfile room2 = new StudySpaceProfile();
        room2.setId("room2");
        room2.setRoomLocation("Library");

        when(studySpaceRepo.findAll()).thenReturn(List.of(room, room2));

        List<OccupancyRecord> low = List.of(
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY)
        );

        List<OccupancyRecord> high = List.of(
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(low)));
        when(historicRepo.findById("room2")).thenReturn(Optional.of(createRecord(high)));

        List<RoomUtilisationDTO> result = service.getMostUsedRooms();

        assertEquals("room2", result.get(0).getRoomId());
    }

    @Test
    void getLeastUsedRooms_sortedAscending() {

        StudySpaceProfile room2 = new StudySpaceProfile();
        room2.setId("room2");
        room2.setRoomLocation("Library");

        when(studySpaceRepo.findAll()).thenReturn(List.of(room, room2));

        List<OccupancyRecord> low = List.of(
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY)
        );

        List<OccupancyRecord> high = List.of(
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(low)));
        when(historicRepo.findById("room2")).thenReturn(Optional.of(createRecord(high)));

        List<RoomUtilisationDTO> result = service.getLeastUsedRooms();

        assertEquals("room1", result.get(0).getRoomId());
    }

    @Test
    void getUnderUtilisedRooms_filtersCorrectly() {

        when(studySpaceRepo.findAll()).thenReturn(List.of(room));

        List<OccupancyRecord> records = List.of(
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));

        List<RoomUtilisationDTO> result = service.getUnderUtilisedRooms();

        assertEquals(1, result.size());
    }

    // ------------------------------------------------
    // WHITE BOX TESTS
    // ------------------------------------------------

    @Test
    void getRoomUtilisationSummary_repositoryException_returnsDataSourceOffline() {

        when(studySpaceRepo.findAll()).thenReturn(List.of(room));
        when(historicRepo.findById("room1")).thenThrow(new RuntimeException());

        List<RoomUtilisationDTO> result = service.getRoomUtilisationSummary();

        assertEquals(AnalyticsDataWarning.DATA_SOURCE_OFFLINE, result.get(0).getWarning());
    }

    @Test
    void getPeakUsageForRoom_insufficientData() {

        List<OccupancyRecord> records = List.of(
                record(Occupancy.LOW),
                record(Occupancy.LOW)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));

        PeakUsageDTO result = service.getPeakUsageForRoom("room1");

        assertEquals(AnalyticsDataWarning.INSUFFICIENT_DATA, result.getWarning());
    }

    @Test
    void getHourlyGraphData_returns24Points() {

        List<OccupancyRecord> records = List.of(
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));

        List<OccupancyGraphPointDTO> result = service.getHourlyGraphData("room1");

        assertEquals(24, result.size());
    }

    @Test
    void getWeeklyGraphData_returns7Days() {

        List<OccupancyRecord> records = List.of(
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));

        List<OccupancyGraphPointDTO> result = service.getWeeklyGraphData("room1");

        assertEquals(7, result.size());
    }

    @Test
    void getBuildingUtilisationSummary_groupsBuildings() {

        StudySpaceProfile room2 = new StudySpaceProfile();
        room2.setId("room2");
        room2.setRoomLocation("Library");

        when(studySpaceRepo.findAll()).thenReturn(List.of(room, room2));

        List<OccupancyRecord> records = List.of(
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));
        when(historicRepo.findById("room2")).thenReturn(Optional.of(createRecord(records)));

        List<BuildingUtilisationDTO> result = service.getBuildingUtilisationSummary();

        assertEquals(1, result.size());
        assertEquals("Library", result.get(0).getBuilding());
    }


    @Test
    void getRoomUtilisationSummary_lessThanFiveRecords_setsWarning() {

        List<OccupancyRecord> records = List.of(
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH)
        );

        when(studySpaceRepo.findAll()).thenReturn(List.of(room));
        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));

        List<RoomUtilisationDTO> result = service.getRoomUtilisationSummary();

        assertEquals(AnalyticsDataWarning.INSUFFICIENT_DATA, result.get(0).getWarning());
    }

    @Test
    void getPeakUsageForRoom_returnsTop3HoursAndDays() {

        List<OccupancyRecord> records = List.of(
                record(Occupancy.HIGH, 10, 1),
                record(Occupancy.HIGH, 10, 1),
                record(Occupancy.HIGH, 12, 2),
                record(Occupancy.HIGH, 12, 2),
                record(Occupancy.HIGH, 14, 3),
                record(Occupancy.HIGH, 14, 3)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(records)));

        PeakUsageDTO result = service.getPeakUsageForRoom("room1");

        assertEquals(3, result.getBusiestTimes().size());
        assertEquals(3, result.getBusiestDays().size());
        assertEquals(AnalyticsDataWarning.NONE, result.getWarning());
    }

    @Test
    void getPeakUsageForRoom_repositoryException_returnsOfflineWarning() {

        when(historicRepo.findById("room1")).thenThrow(new RuntimeException());

        PeakUsageDTO result = service.getPeakUsageForRoom("room1");

        assertEquals(AnalyticsDataWarning.DATA_SOURCE_OFFLINE, result.getWarning());
    }

    @Test
    void getHourlyGraphData_noRecords_returnsEmptyList() {

        when(historicRepo.findById("room1")).thenReturn(Optional.empty());

        List<OccupancyGraphPointDTO> result = service.getHourlyGraphData("room1");

        assertTrue(result.isEmpty());
    }

    @Test
    void getHourlyGraphData_repositoryException_returnsEmptyList() {

        when(historicRepo.findById("room1")).thenThrow(new RuntimeException());

        List<OccupancyGraphPointDTO> result = service.getHourlyGraphData("room1");

        assertTrue(result.isEmpty());
    }

    @Test
    void getWeeklyGraphData_noRecords_returnsEmptyList() {

        when(historicRepo.findById("room1")).thenReturn(Optional.empty());

        List<OccupancyGraphPointDTO> result = service.getWeeklyGraphData("room1");

        assertTrue(result.isEmpty());
    }

    @Test
    void getWeeklyGraphData_repositoryException_returnsEmptyList() {

        when(historicRepo.findById("room1")).thenThrow(new RuntimeException());

        List<OccupancyGraphPointDTO> result = service.getWeeklyGraphData("room1");

        assertTrue(result.isEmpty());
    }

    @Test
    void getBuildingUtilisationSummary_skipsRoomsWithNoRecords() {

        when(studySpaceRepo.findAll()).thenReturn(List.of(room));
        when(historicRepo.findById("room1")).thenReturn(Optional.empty());

        List<BuildingUtilisationDTO> result = service.getBuildingUtilisationSummary();

        assertTrue(result.isEmpty());
    }

    @Test
    void getBuildingUtilisationSummary_multipleBuildings_sorted() {

        StudySpaceProfile room2 = new StudySpaceProfile();
        room2.setId("room2");
        room2.setRoomLocation("Science");

        when(studySpaceRepo.findAll()).thenReturn(List.of(room, room2));

        List<OccupancyRecord> high = List.of(
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH),
                record(Occupancy.HIGH)
        );

        List<OccupancyRecord> low = List.of(
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY),
                record(Occupancy.EMPTY)
        );

        when(historicRepo.findById("room1")).thenReturn(Optional.of(createRecord(high)));
        when(historicRepo.findById("room2")).thenReturn(Optional.of(createRecord(low)));

        List<BuildingUtilisationDTO> result = service.getBuildingUtilisationSummary();

        assertEquals("Library", result.get(0).getBuilding());
    }



}