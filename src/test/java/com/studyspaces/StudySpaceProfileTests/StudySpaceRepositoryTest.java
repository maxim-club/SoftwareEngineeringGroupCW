package com.studyspaces.StudySpaceProfileTests;

import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudySpaceRepositoryMockTest {

    @Mock
    private StudySpaceRepository repository;

    private StudySpaceProfile profile1;
    private StudySpaceProfile profile2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        profile1 = new StudySpaceProfile();
        profile1.setId("1");
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
        profile2.setId("2");
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
    }

    @Test
    void testFindById() {
        when(repository.findById("1")).thenReturn(Optional.of(profile1));

        Optional<StudySpaceProfile> found = repository.findById("1");
        assertTrue(found.isPresent());
        assertEquals("Room A", found.get().getRoomLocation());
    }

    @Test
    void testFindFirstByRoomLocation() {
        when(repository.findFirstByRoomLocation("Room A")).thenReturn(Optional.of(profile1));

        Optional<StudySpaceProfile> found = repository.findFirstByRoomLocation("Room A");
        assertTrue(found.isPresent());
        assertEquals("Room A", found.get().getRoomLocation());
    }

    @Test
    void testFindByOccupancy() {
        when(repository.findByOccupancy(Occupancy.EMPTY)).thenReturn(List.of(profile1));

        List<StudySpaceProfile> emptyRooms = repository.findByOccupancy(Occupancy.EMPTY);
        assertEquals(1, emptyRooms.size());
        assertEquals("Room A", emptyRooms.get(0).getRoomLocation());
    }

    @Test
    void testFindBySuitableForGroupsTrue() {
        when(repository.findBySuitableForGroupsTrue()).thenReturn(List.of(profile1));

        List<StudySpaceProfile> groupRooms = repository.findBySuitableForGroupsTrue();
        assertEquals(1, groupRooms.size());
        assertEquals("Room A", groupRooms.get(0).getRoomLocation());
    }

    @Test
    void testFindByAmenitiesPlugSocketsTrue() {
        when(repository.findByAmenitiesPlugSocketsTrue()).thenReturn(List.of(profile1));

        List<StudySpaceProfile> plugRooms = repository.findByAmenitiesPlugSocketsTrue();
        assertEquals(1, plugRooms.size());
        assertEquals("Room A", plugRooms.get(0).getRoomLocation());
    }

    @Test
    void testFindByNotesContaining() {
        when(repository.findByNotesContaining("Quiet")).thenReturn(List.of(profile1));

        List<StudySpaceProfile> notes = repository.findByNotesContaining("Quiet");
        assertEquals(1, notes.size());
        assertEquals("Room A", notes.get(0).getRoomLocation());
    }

    @Test
    void testDeleteByRoomLocation() {
        doNothing().when(repository).deleteByRoomLocation("Room B");
        when(repository.findFirstByRoomLocation("Room B")).thenReturn(Optional.empty());

        repository.deleteByRoomLocation("Room B");
        Optional<StudySpaceProfile> found = repository.findFirstByRoomLocation("Room B");

        assertTrue(found.isEmpty());
        verify(repository).deleteByRoomLocation("Room B");
    }

    @Test
    void testFindByRoomLocationContaining() {
        when(repository.findByRoomLocationContaining("Room")).thenReturn(List.of(profile1, profile2));

        List<StudySpaceProfile> rooms = repository.findByRoomLocationContaining("Room");
        assertEquals(2, rooms.size());
    }
}
