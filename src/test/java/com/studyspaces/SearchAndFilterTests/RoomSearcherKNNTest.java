package com.studyspaces.SearchAndFilterTests;

import com.studyspaces.spacefinder.model.FilterQuery;
import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.service.RoomSearcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.util.Pair;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomSearcherKNNTest {

    private StudySpaceRepository repository;

    @BeforeEach
    void setup() {
        repository = mock(StudySpaceRepository.class);

        // inject mocked repo into static RoomSearcher
        new RoomSearcher(repository);
    }

    /**
     * Helper to build a FilterQuery whose vector distance
     * ordering we can control.
     */
    private FilterQuery makeQuery(float groupSizeValue) {
        FilterQuery q = new FilterQuery();
        q.preferredGroupSize = (int)(groupSizeValue * 10); // matches /10f normalisation
        return q;
    }

    @Test
    void getKRecommended_returnsClosestRoomsInOrder() {

        // ---- mock rooms ----
        StudySpaceProfile r1 = mock(StudySpaceProfile.class);
        StudySpaceProfile r2 = mock(StudySpaceProfile.class);
        StudySpaceProfile r3 = mock(StudySpaceProfile.class);

        when(r1.getId()).thenReturn("roomA");
        when(r2.getId()).thenReturn("roomB");
        when(r3.getId()).thenReturn("roomC");

        // control vector distances via group size
        when(r1.toFilterQuery()).thenReturn(makeQuery(0.0f)); // closest
        when(r2.toFilterQuery()).thenReturn(makeQuery(0.3f));
        when(r3.toFilterQuery()).thenReturn(makeQuery(1.0f)); // farthest

        when(repository.findAll()).thenReturn(List.of(r1, r2, r3));

        // build search space using real code
        RoomSearcher.initialiseSearchSpace();

        // query near 0
        FilterQuery userQuery = makeQuery(0.0f);

        List<String> result =
                RoomSearcher.getKRecommended(userQuery, 2);

        assertEquals(2, result.size());
        assertEquals("roomA", result.get(0));
        assertEquals("roomB", result.get(1));
    }

    @Test
    void getKRecommended_kGreaterThanDataset_returnsAllRooms() {

        StudySpaceProfile r1 = mock(StudySpaceProfile.class);
        StudySpaceProfile r2 = mock(StudySpaceProfile.class);

        when(r1.getId()).thenReturn("room1");
        when(r2.getId()).thenReturn("room2");

        when(r1.toFilterQuery()).thenReturn(makeQuery(0.2f));
        when(r2.toFilterQuery()).thenReturn(makeQuery(0.8f));

        when(repository.findAll()).thenReturn(List.of(r1, r2));

        RoomSearcher.initialiseSearchSpace();

        List<String> result =
                RoomSearcher.getKRecommended(makeQuery(0.1f), 10);

        assertEquals(2, result.size());
        assertTrue(result.contains("room1"));
        assertTrue(result.contains("room2"));
    }

    @Test
    void getKRecommended_emptyRepository_returnsEmptyList() {

        when(repository.findAll()).thenReturn(List.of());

        RoomSearcher.initialiseSearchSpace();

        List<String> result =
                RoomSearcher.getKRecommended(new FilterQuery(), 5);

        assertTrue(result.isEmpty());
    }

    @Test
    void getKRecommended_resultsAreSortedByDistance() {

        StudySpaceProfile near = mock(StudySpaceProfile.class);
        StudySpaceProfile mid  = mock(StudySpaceProfile.class);
        StudySpaceProfile far  = mock(StudySpaceProfile.class);

        when(near.getId()).thenReturn("near");
        when(mid.getId()).thenReturn("mid");
        when(far.getId()).thenReturn("far");

        when(near.toFilterQuery()).thenReturn(makeQuery(0.1f));
        when(mid.toFilterQuery()).thenReturn(makeQuery(0.5f));
        when(far.toFilterQuery()).thenReturn(makeQuery(1.0f));

        when(repository.findAll()).thenReturn(List.of(near, mid, far));

        RoomSearcher.initialiseSearchSpace();

        List<String> result =
                RoomSearcher.getKRecommended(makeQuery(0.0f), 3);

        assertEquals(List.of("near", "mid", "far"), result);
    }

    @Test
    void getSortedByRecommended_returnsAllRoomsSortedByDistance() {

        // ---- mock rooms ----
        StudySpaceProfile near = mock(StudySpaceProfile.class);
        StudySpaceProfile mid  = mock(StudySpaceProfile.class);
        StudySpaceProfile far  = mock(StudySpaceProfile.class);

        when(near.getId()).thenReturn("near");
        when(mid.getId()).thenReturn("mid");
        when(far.getId()).thenReturn("far");

        // control distance ordering via group size
        when(near.toFilterQuery()).thenReturn(makeQuery(0.1f)); // closest
        when(mid.toFilterQuery()).thenReturn(makeQuery(0.5f));
        when(far.toFilterQuery()).thenReturn(makeQuery(1.0f));  // farthest

        when(repository.findAll()).thenReturn(List.of(near, mid, far));

        // build search space
        RoomSearcher.initialiseSearchSpace();

        // query near 0
        FilterQuery userQuery = makeQuery(0.0f);

        List<String> result =
                RoomSearcher.getSortedByRecommended(userQuery);

        // ---- assertions ----
        assertEquals(3, result.size());
        assertEquals(List.of("near", "mid", "far"), result);
    }

    @Test
    void getSortedByRecommended_emptyRepository_returnsEmptyList() {

        when(repository.findAll()).thenReturn(List.of());

        RoomSearcher.initialiseSearchSpace();

        List<String> result =
                RoomSearcher.getSortedByRecommended(new FilterQuery());

        assertTrue(result.isEmpty());
    }
}