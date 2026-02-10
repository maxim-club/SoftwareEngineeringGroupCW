package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.SpacefinderApplication;

import com.studyspaces.spacefinder.StudySpaceProfile;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpacefinderApplication.class)
public class StudySpaceProfileManagerTest {

    private StudySpaceProfileManager manager;

    @Autowired
    private StudySpaceRepository repository;

    private StudySpaceProfile profile1;
    private StudySpaceProfile profile2;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        manager = new StudySpaceProfileManager(repository);

        // Profile 1
        profile1 = new StudySpaceProfile();
        profile1.setRoomLocation("Room A");
        profile1.setOccupancy(Occupancy.EMPTY);
        profile1.setNoiseLevel(NoiseLevel.SILENT);
        profile1.setSuitableForGroups(true);
        profile1.setMaxGroupSize(5);
        profile1.setNotes("Quiet room for study");
        profile1.setAmenities(new Amenities(true, true, false, false, false, false, false, true));
        profile1.setSchedule(new ArrayList<>());
        repository.save(profile1);

        // Profile 2
        profile2 = new StudySpaceProfile();
        profile2.setRoomLocation("Room B");
        profile2.setOccupancy(Occupancy.BUSY);
        profile2.setNoiseLevel(NoiseLevel.LOUD);
        profile2.setSuitableForGroups(false);
        profile2.setMaxGroupSize(1);
        profile2.setNotes("Busy room");
        profile2.setAmenities(new Amenities(false, false, true, false, true, false, false, true));
        profile2.setSchedule(new ArrayList<>());
        repository.save(profile2);
    }

    @Test
    void testSaveAndGetById() {
        StudySpaceProfile saved = manager.save(profile1);
        Optional<StudySpaceProfile> fetched = manager.getById(saved.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Room A", fetched.get().getRoomLocation());
    }

    @Test
    void testReplaceProfile() {
        StudySpaceProfile replacement = new StudySpaceProfile();
        replacement.setRoomLocation("Room A Updated");
        replacement.setOccupancy(Occupancy.MODERATE);
        replacement.setNoiseLevel(NoiseLevel.MODERATE);
        replacement.setSuitableForGroups(true);
        replacement.setMaxGroupSize(10);
        replacement.setNotes("Updated notes");
        replacement.setAmenities(new Amenities(true,true,true,true,true,true,true, false));
        replacement.setSchedule(new ArrayList<>());

        StudySpaceProfile replaced = manager.replace(profile1.getId(), replacement);
        assertEquals("Room A Updated", replaced.getRoomLocation());
        assertEquals(Occupancy.MODERATE, replaced.getOccupancy());
        assertEquals(10, replaced.getMaxGroupSize());
    }

    @Test
    void testGetAll() {
        List<StudySpaceProfile> allProfiles = manager.getAll();
        assertEquals(2, allProfiles.size());
    }

    @Test
    void testGetByRoomLocation() {
        List<StudySpaceProfile> results = manager.getByRoomLocation("Room A");
        assertEquals(1, results.size());
        assertEquals("Room A", results.get(0).getRoomLocation());
    }

    @Test
    void testGetByOccupancy() {
        List<StudySpaceProfile> emptyRooms = manager.getByOccupancy(Occupancy.EMPTY);
        assertEquals(1, emptyRooms.size());
        assertEquals("Room A", emptyRooms.get(0).getRoomLocation());
    }

    @Test
    void testGetSuitableForGroups() {
        List<StudySpaceProfile> groupRooms = manager.getSuitableForGroups();
        assertEquals(1, groupRooms.size());
        assertTrue(groupRooms.get(0).isSuitableForGroups());
    }

    @Test
    void testDeleteById() {
        manager.deleteById(profile1.getId());
        assertFalse(manager.getById(profile1.getId()).isPresent());
    }

    @Test
    void testSearchNotes() {
        List<StudySpaceProfile> results = manager.searchNotes("Quiet");
        assertEquals(1, results.size());
        assertEquals("Room A", results.get(0).getRoomLocation());
    }

    @Test
    void testAmenitiesHelpers() {
        List<StudySpaceProfile> withSockets = manager.getWithPlugSockets();
        assertEquals(1, withSockets.size());
        assertEquals("Room A", withSockets.get(0).getRoomLocation());

        List<StudySpaceProfile> withComputers = manager.getWithComputers();
        assertEquals(1, withComputers.size());
        assertEquals("Room B", withComputers.get(0).getRoomLocation());
    }
}
