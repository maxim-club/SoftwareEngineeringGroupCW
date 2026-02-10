package com.studyspaces.StudySpaceProfileTests;

import com.studyspaces.spacefinder.SpacefinderApplication;
import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpacefinderApplication.class)
public class StudySpaceRepositoryTest {

    @Autowired
    private StudySpaceRepository repository;

    private StudySpaceProfile profile1;
    private StudySpaceProfile profile2;

    @BeforeEach
    void setup() {
        repository.deleteAll(); // clear DB before each test

        profile1 = new StudySpaceProfile();
        profile1.setRoomLocation("Room A");
        profile1.setOccupancy(Occupancy.EMPTY);
        profile1.setNoiseLevel(NoiseLevel.SILENT);
        profile1.setSuitableForGroups(true);
        profile1.setMaxGroupSize(5);
        profile1.setNotes("Quiet room for study");

        Amenities amenities1 = new Amenities();
        amenities1.setPlugSockets(true);
        amenities1.setDesks(true);
        profile1.setAmenities(amenities1);

        profile2 = new StudySpaceProfile();
        profile2.setRoomLocation("Room B");
        profile2.setOccupancy(Occupancy.BUSY);
        profile2.setNoiseLevel(NoiseLevel.LOUD);
        profile2.setSuitableForGroups(false);
        profile2.setMaxGroupSize(1);
        profile2.setNotes("Busy room");

        Amenities amenities2 = new Amenities();
        amenities2.setComputers(true);
        amenities2.setFoodAllowed(true);
        profile2.setAmenities(amenities2);

        repository.save(profile1);
        repository.save(profile2);
    }

    @Test
    void testFindById() {
        Optional<StudySpaceProfile> found = repository.findById(profile1.getId());
        assertTrue(found.isPresent());
        assertEquals("Room A", found.get().getRoomLocation());
    }

    @Test
    void testFindFirstByRoomLocation() {
        Optional<StudySpaceProfile> found = repository.findFirstByRoomLocation("Room A");
        assertTrue(found.isPresent());
        assertEquals("Room A", found.get().getRoomLocation());
    }

    @Test
    void testFindByOccupancy() {
        List<StudySpaceProfile> emptyRooms = repository.findByOccupancy(Occupancy.EMPTY);
        assertEquals(1, emptyRooms.size());
        assertEquals("Room A", emptyRooms.get(0).getRoomLocation());
    }

    @Test
    void testFindBySuitableForGroupsTrue() {
        List<StudySpaceProfile> groupRooms = repository.findBySuitableForGroupsTrue();
        assertEquals(1, groupRooms.size());
        assertEquals("Room A", groupRooms.get(0).getRoomLocation());
    }

    @Test
    void testFindByAmenitiesPlugSocketsTrue() {
        List<StudySpaceProfile> plugRooms = repository.findByAmenitiesPlugSocketsTrue();
        assertEquals(1, plugRooms.size());
        assertEquals("Room A", plugRooms.get(0).getRoomLocation());
    }

    @Test
    void testFindByNotesContaining() {
        List<StudySpaceProfile> notes = repository.findByNotesContaining("Quiet");
        assertEquals(1, notes.size());
        assertEquals("Room A", notes.get(0).getRoomLocation());
    }

    @Test
    void testDeleteByRoomLocation() {
        repository.deleteByRoomLocation("Room B");
        Optional<StudySpaceProfile> found = repository.findFirstByRoomLocation("Room B");
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByRoomLocationContaining() {
        List<StudySpaceProfile> rooms = repository.findByRoomLocationContaining("Room");
        assertEquals(2, rooms.size());
    }
}

