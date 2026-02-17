package com.studyspaces.SearchAndFilterTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.studyspaces.spacefinder.model.FilterQuery;
import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.service.RoomSearcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.*;

class RoomSearcherInitialiseTest {

    private StudySpaceRepository repository;

    @BeforeEach
    void setup() {
        repository = mock(StudySpaceRepository.class);
        new RoomSearcher(repository); // inject mock repository
    }

    @Test
    void initialiseSearchSpace_emptyRepository_createsEmptyMap() {

        when(repository.findAll()).thenReturn(Collections.emptyList());

        RoomSearcher.initialiseSearchSpace();


        Map<String, List<Pair<Integer, Float>>> space =
                RoomSearcher.getSearchSpace();

        assertNotNull(space);
        assertTrue(space.isEmpty());
    }

    @Test
    void initialiseSearchSpace_populatesAllRooms() {

        StudySpaceProfile r1 = mock(StudySpaceProfile.class);
        StudySpaceProfile r2 = mock(StudySpaceProfile.class);

        when(r1.getId()).thenReturn("room1");
        when(r2.getId()).thenReturn("room2");

        when(r1.toFilterQuery()).thenReturn(new FilterQuery());
        when(r2.toFilterQuery()).thenReturn(new FilterQuery());

        when(repository.findAll()).thenReturn(List.of(r1, r2));

        RoomSearcher.initialiseSearchSpace();

        Map<String, List<Pair<Integer, Float>>> space =
                RoomSearcher.getSearchSpace();

        assertEquals(2, space.size());
        assertTrue(space.containsKey("room1"));
        assertTrue(space.containsKey("room2"));
    }

    @Test
    void initialiseSearchSpace_storesCorrectVectorForRoom() {

        StudySpaceProfile room = mock(StudySpaceProfile.class);
        FilterQuery query = new FilterQuery();

        when(room.getId()).thenReturn("roomA");
        when(room.toFilterQuery()).thenReturn(query);
        when(repository.findAll()).thenReturn(List.of(room));

        List<Pair<Integer, Float>> expectedVector =
                RoomSearcher.Vectorise(query);

        RoomSearcher.initialiseSearchSpace();

        Map<String, List<Pair<Integer, Float>>> space =
                RoomSearcher.getSearchSpace();

        assertEquals(expectedVector, space.get("roomA"));
    }

    @Test
    void initialiseSearchSpace_duplicateIds_overwritesEntry() {

        StudySpaceProfile r1 = mock(StudySpaceProfile.class);
        StudySpaceProfile r2 = mock(StudySpaceProfile.class);

        when(r1.getId()).thenReturn("sameID");
        when(r2.getId()).thenReturn("sameID");

        when(r1.toFilterQuery()).thenReturn(new FilterQuery());
        when(r2.toFilterQuery()).thenReturn(new FilterQuery());

        when(repository.findAll()).thenReturn(List.of(r1, r2));

        RoomSearcher.initialiseSearchSpace();

        Map<String, List<Pair<Integer, Float>>> space =
                RoomSearcher.getSearchSpace();

        assertEquals(1, space.size());
    }
}