package com.studyspaces.StudySpaceProfileTests;

import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.service.StudySpaceProfileManager;

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

class StudySpaceProfileManagerMockTest {

    @Mock
    private StudySpaceRepository repository;

    @InjectMocks
    private StudySpaceProfileManager manager;

    private StudySpaceProfile profile1;
    private StudySpaceProfile profile2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Profile 1
        profile1 = new StudySpaceProfile();
        profile1.setId("1");
        profile1.setRoomLocation("Room A");
        profile1.setOccupancy(Occupancy.EMPTY);
        profile1.setNoiseLevel(NoiseLevel.SILENT);
        profile1.setSuitableForGroups(true);
        profile1.setMaxGroupSize(5);
        profile1.setNotes("Quiet room for study");
        profile1.setAmenities(Amenities.builder()
                .desks(true)
                .computers(true)
                .foodAllowed(true)
                .heaters(true)
                .monitors(true)
                .naturalLight(false)
                .plugSockets(true)
                .printers(true)
                .projectors(true)
                .silent(true)
                .toiletNearby(true)
                .waterFountainNearby(true)
                .wheelchairAccessible(true)
                .whiteboard(true)
                .build());
        profile1.setSchedule(new ArrayList<>());

        // Profile 2
        profile2 = new StudySpaceProfile();
        profile2.setId("2");
        profile2.setRoomLocation("Room B");
        profile2.setOccupancy(Occupancy.HIGH);
        profile2.setNoiseLevel(NoiseLevel.LOUD);
        profile2.setSuitableForGroups(false);
        profile2.setMaxGroupSize(1);
        profile2.setNotes("Busy room");
        profile2.setAmenities(Amenities.builder()
                .desks(true)
                .computers(true)
                .foodAllowed(true)
                .heaters(true)
                .monitors(true)
                .naturalLight(false)
                .plugSockets(true)
                .printers(true)
                .projectors(true)
                .silent(true)
                .toiletNearby(true)
                .waterFountainNearby(true)
                .wheelchairAccessible(true)
                .whiteboard(true)
                .build());
        profile2.setSchedule(new ArrayList<>());
    }

    @Test
    void testSaveAndGetById() {
        when(repository.save(profile1)).thenReturn(profile1);
        when(repository.findById("1")).thenReturn(Optional.of(profile1));

        StudySpaceProfile saved = manager.save(profile1);
        Optional<StudySpaceProfile> fetched = manager.getById("1");

        assertTrue(fetched.isPresent());
        assertEquals("Room A", fetched.get().getRoomLocation());
        verify(repository).save(profile1);
        verify(repository).findById("1");
    }

	@Test
	void testReplaceProfile() {
		// Replacement data (id is ignored)
		StudySpaceProfile replacement = new StudySpaceProfile();
		replacement.setRoomLocation("Room A Updated");
		replacement.setOccupancy(Occupancy.MEDIUM);
		replacement.setNoiseLevel(NoiseLevel.MODERATE);
		replacement.setSuitableForGroups(true);
		replacement.setMaxGroupSize(10);
		replacement.setNotes("Updated notes");
		replacement.setAmenities(Amenities.builder()
                .desks(true)
                .computers(true)
                .foodAllowed(true)
                .heaters(true)
                .monitors(true)
                .naturalLight(false)
                .plugSockets(true)
                .printers(true)
                .projectors(true)
                .silent(true)
                .toiletNearby(true)
                .waterFountainNearby(true)
                .wheelchairAccessible(true)
                .whiteboard(true)
                .build());
		replacement.setSchedule(new ArrayList<>());

		// Mock existing profile in repository
		when(repository.findById("1")).thenReturn(Optional.of(profile1));

		// Mock save: return the profile passed in
		when(repository.save(any(StudySpaceProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Perform replace
		StudySpaceProfile replaced = manager.replace("1", replacement);

		// Capture what was saved
		ArgumentCaptor<StudySpaceProfile> captor = ArgumentCaptor.forClass(StudySpaceProfile.class);
		verify(repository).save(captor.capture());
		StudySpaceProfile saved = captor.getValue();

		// Verify id is preserved
		assertEquals("1", saved.getId());

		// Verify fields updated
		assertEquals("Room A Updated", saved.getRoomLocation());
		assertEquals("Updated notes", saved.getNotes());
		assertEquals(Occupancy.MEDIUM, saved.getOccupancy());
		assertEquals(NoiseLevel.MODERATE, saved.getNoiseLevel());
		assertTrue(saved.isSuitableForGroups());
		assertEquals(10, saved.getMaxGroupSize());

		// Optional: verify amenities
		assertNotNull(saved.getAmenities());
		assertTrue(saved.getAmenities().isPlugSockets());
		assertTrue(saved.getAmenities().isDesks());
		assertTrue(saved.getAmenities().isComputers());
		assertTrue(saved.getAmenities().isPrinters());
		assertTrue(saved.getAmenities().isFoodAllowed());
		assertTrue(saved.getAmenities().isWaterFountainNearby());
		assertTrue(saved.getAmenities().isToiletNearby());
		assertTrue(saved.getAmenities().isWheelchairAccessible());

		// Verify repository interactions
		verify(repository).findById("1");
		verify(repository).save(any(StudySpaceProfile.class));
	}

    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(List.of(profile1, profile2));

        List<StudySpaceProfile> allProfiles = manager.getAll();
        assertEquals(2, allProfiles.size());
        verify(repository).findAll();
    }

    @Test
    void testGetByRoomLocation() {
        when(repository.findByRoomLocation("Room A")).thenReturn(List.of(profile1));

        List<StudySpaceProfile> results = manager.getByRoomLocation("Room A");
        assertEquals(1, results.size());
        assertEquals("Room A", results.get(0).getRoomLocation());
        verify(repository).findByRoomLocation("Room A");
    }

    @Test
    void testGetByOccupancy() {
        when(repository.findByOccupancy(Occupancy.EMPTY)).thenReturn(List.of(profile1));

        List<StudySpaceProfile> emptyRooms = manager.getByOccupancy(Occupancy.EMPTY);
        assertEquals(1, emptyRooms.size());
        assertEquals("Room A", emptyRooms.get(0).getRoomLocation());
        verify(repository).findByOccupancy(Occupancy.EMPTY);
    }

    @Test
    void testGetSuitableForGroups() {
        when(repository.findBySuitableForGroupsTrue()).thenReturn(List.of(profile1));

        List<StudySpaceProfile> groupRooms = manager.getSuitableForGroups();
        assertEquals(1, groupRooms.size());
        assertTrue(groupRooms.get(0).isSuitableForGroups());
        verify(repository).findBySuitableForGroupsTrue();
    }

    @Test
    void testDeleteById() {
        doNothing().when(repository).deleteById("1");
        when(repository.findById("1")).thenReturn(Optional.empty());

        manager.deleteById("1");
        Optional<StudySpaceProfile> fetched = manager.getById("1");

        assertFalse(fetched.isPresent());
        verify(repository).deleteById("1");
        verify(repository).findById("1");
    }

    @Test
    void testSearchNotes() {
        when(repository.findByNotesContaining("Quiet")).thenReturn(List.of(profile1));

        List<StudySpaceProfile> results = manager.searchNotes("Quiet");
        assertEquals(1, results.size());
        assertEquals("Room A", results.get(0).getRoomLocation());
        verify(repository).findByNotesContaining("Quiet");
    }

    @Test
    void testAmenitiesHelpers() {
        when(repository.findByAmenitiesPlugSocketsTrue()).thenReturn(List.of(profile1));
        when(repository.findByAmenitiesComputersTrue()).thenReturn(List.of(profile2));

        List<StudySpaceProfile> withSockets = manager.getWithPlugSockets();
        assertEquals(1, withSockets.size());
        assertEquals("Room A", withSockets.get(0).getRoomLocation());

        List<StudySpaceProfile> withComputers = manager.getWithComputers();
        assertEquals(1, withComputers.size());
        assertEquals("Room B", withComputers.get(0).getRoomLocation());

        verify(repository).findByAmenitiesPlugSocketsTrue();
        verify(repository).findByAmenitiesComputersTrue();
    }

    @Test
    void testReplaceProfileNotFound() {

        StudySpaceProfile replacement = new StudySpaceProfile();
        replacement.setRoomLocation("New Room");

        when(repository.findById("999")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> manager.replace("999", replacement));

        assertEquals("Profile not found with id: 999", ex.getMessage());

        verify(repository).findById("999");
        verify(repository, never()).save(any());
    }

    @Test
    void testGetByIds() {

        when(repository.findByIds(List.of("1","2")))
                .thenReturn(List.of(profile1, profile2));

        List<StudySpaceProfile> result = manager.getByIds(List.of("1","2"));

        assertEquals(2, result.size());
        verify(repository).findByIds(List.of("1","2"));
    }


    @Test
    void testGetFirstByRoomLocation() {

        when(repository.findFirstByRoomLocation("Room A"))
                .thenReturn(Optional.of(profile1));

        Optional<StudySpaceProfile> result =
                manager.getFirstByRoomLocation("Room A");

        assertTrue(result.isPresent());
        assertEquals("Room A", result.get().getRoomLocation());

        verify(repository).findFirstByRoomLocation("Room A");
    }

    @Test
    void testSearchByRoomLocationKeyword() {

        when(repository.findByRoomLocationContaining("Room"))
                .thenReturn(List.of(profile1, profile2));

        List<StudySpaceProfile> result =
                manager.searchByRoomLocationKeyword("Room");

        assertEquals(2, result.size());

        verify(repository).findByRoomLocationContaining("Room");
    }

    @Test
    void testGetByOccupancyIn() {

        when(repository.findByOccupancyIn(List.of(Occupancy.EMPTY, Occupancy.HIGH)))
                .thenReturn(List.of(profile1, profile2));

        List<StudySpaceProfile> result =
                manager.getByOccupancyIn(List.of(Occupancy.EMPTY, Occupancy.HIGH));

        assertEquals(2, result.size());

        verify(repository).findByOccupancyIn(any());
    }

    @Test
    void testGetByNoiseLevel() {

        when(repository.findByNoiseLevel(NoiseLevel.SILENT))
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result =
                manager.getByNoiseLevel(NoiseLevel.SILENT);

        assertEquals(1, result.size());

        verify(repository).findByNoiseLevel(NoiseLevel.SILENT);
    }

    @Test
    void testGetByNoiseLevelIn() {

        when(repository.findByNoiseLevelIn(List.of(NoiseLevel.SILENT, NoiseLevel.LOUD)))
                .thenReturn(List.of(profile1, profile2));

        List<StudySpaceProfile> result =
                manager.getByNoiseLevelIn(List.of(NoiseLevel.SILENT, NoiseLevel.LOUD));

        assertEquals(2, result.size());

        verify(repository).findByNoiseLevelIn(any());
    }

    @Test
    void testGetNotSuitableForGroups() {

        when(repository.findBySuitableForGroupsFalse())
                .thenReturn(List.of(profile2));

        List<StudySpaceProfile> result =
                manager.getNotSuitableForGroups();

        assertEquals(1, result.size());

        verify(repository).findBySuitableForGroupsFalse();
    }


    @Test
    void testGetByMaxGroupSizeGreaterThan() {

        when(repository.findByMaxGroupSizeGreaterThan(2))
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result =
                manager.getByMaxGroupSizeGreaterThan(2);

        assertEquals(1, result.size());

        verify(repository).findByMaxGroupSizeGreaterThan(2);
    }

    @Test
    void testGetByMaxGroupSizeLessThanEqual() {

        when(repository.findByMaxGroupSizeLessThanEqual(1))
                .thenReturn(List.of(profile2));

        List<StudySpaceProfile> result =
                manager.getByMaxGroupSizeLessThanEqual(1);

        assertEquals(1, result.size());

        verify(repository).findByMaxGroupSizeLessThanEqual(1);
    }

    @Test
    void testGetWithDesks() {

        when(repository.findByAmenitiesDesksTrue())
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result = manager.getWithDesks();

        assertEquals(1, result.size());

        verify(repository).findByAmenitiesDesksTrue();
    }


    @Test
    void testGetWithPrinters() {

        when(repository.findByAmenitiesPrintersTrue())
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result = manager.getWithPrinters();

        assertEquals(1, result.size());

        verify(repository).findByAmenitiesPrintersTrue();
    }

    @Test
    void testGetWithFoodAllowed() {

        when(repository.findByAmenitiesFoodAllowedTrue())
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result = manager.getWithFoodAllowed();

        assertEquals(1, result.size());

        verify(repository).findByAmenitiesFoodAllowedTrue();
    }

    @Test
    void testGetWithToiletNearby() {

        when(repository.findByAmenitiesToiletNearbyTrue())
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result = manager.getWithToiletNearby();

        assertEquals(1, result.size());

        verify(repository).findByAmenitiesToiletNearbyTrue();
    }

    @Test
    void testGetWheelchairAccessible() {

        when(repository.findByAmenitiesWheelchairAccessibleTrue())
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result = manager.getWheelchairAccessible();

        assertEquals(1, result.size());

        verify(repository).findByAmenitiesWheelchairAccessibleTrue();
    }

    @Test
    void testGetByScheduleDay() {

        when(repository.findByScheduleDay(1))
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result =
                manager.getByScheduleDay(1);

        assertEquals(1, result.size());

        verify(repository).findByScheduleDay(1);
    }

    @Test
    void testGetWithSocketsAndDesks() {

        when(repository.findByAmenitiesPlugSocketsTrueAndAmenitiesDesksTrue())
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result = manager.getWithSocketsAndDesks();

        assertEquals(1, result.size());

        verify(repository).findByAmenitiesPlugSocketsTrueAndAmenitiesDesksTrue();
    }

    @Test
    void testGetByScheduleDayAndTimeAfter() {

        when(repository.findByScheduleDayAndScheduleTimeGreaterThan(1, 1200))
                .thenReturn(List.of(profile1));

        List<StudySpaceProfile> result =
                manager.getByScheduleDayAndTimeAfter(1, 1200);

        assertEquals(1, result.size());

        verify(repository)
                .findByScheduleDayAndScheduleTimeGreaterThan(1, 1200);
    }

    @Test
    void testDeleteByRoomLocation() {

        doNothing().when(repository).deleteByRoomLocation("Room A");

        manager.deleteByRoomLocation("Room A");

        verify(repository).deleteByRoomLocation("Room A");
    }

    @Test
    void testDeleteByRoomLocationKeyword() {

        doNothing().when(repository).deleteByRoomLocationContaining("Room");

        manager.deleteByRoomLocationKeyword("Room");

        verify(repository).deleteByRoomLocationContaining("Room");
    }



}
