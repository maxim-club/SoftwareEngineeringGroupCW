package com.studyspaces.SearchAndFilterTests;

import com.studyspaces.spacefinder.service.RoomSearcher;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.service.StudySpaceProfileManager;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterVectoriserTest {

    @Mock
    StudySpaceRepository repo;

    @InjectMocks
    private RoomSearcher roomSearcher;

    private static final int VECTOR_SIZE = 12;
    // 2 enums + 1 bool + 8 amenities + 1 groupSize

    // BLACK BOX TESTS
    @Test
    void emptyQuery_allWeightsZero() {

        FilterQuery query = new FilterQuery();

        List<Pair<Integer, Float>> vector =
                RoomSearcher.Vectorise(query);

        assertEquals(VECTOR_SIZE, vector.size());

        for (Pair<Integer, Float> p : vector) {
            assertEquals(0, p.getFirst());
        }
    }

    @Test
    void noisePreference_setsWeight() {

        FilterQuery query = new FilterQuery();
        query.preferredNoiseLevel = NoiseLevel.QUIET_DISCUSSION;

        var vector = RoomSearcher.Vectorise(query);

        Pair<Integer, Float> noise = vector.get(0);

        assertEquals(1, noise.getFirst());
        assertTrue(noise.getSecond() >= 0f &&
                noise.getSecond() <= 1f);
    }

    @Test
    void nullAmenities_safe() {

        FilterQuery query = new FilterQuery();
        query.preferredAmenities = null;

        var vector = RoomSearcher.Vectorise(query);

        assertEquals(VECTOR_SIZE, vector.size());
    }

    @Test
    void groupSpace_true_encodedCorrectly() {

        FilterQuery query = new FilterQuery();
        query.preferredGroupSpace = true;

        var vector = RoomSearcher.Vectorise(query);

        Pair<Integer, Float> groupSpace = vector.get(2);

        assertEquals(1, groupSpace.getFirst());
        assertEquals(1f, groupSpace.getSecond());
    }

    @Test
    void groupSize_normalisedRange() {

        FilterQuery query = new FilterQuery();
        query.preferredGroupSize = 5;

        var vector = RoomSearcher.Vectorise(query);

        Pair<Integer, Float> groupSize = vector.get(11);

        assertEquals(1, groupSize.getFirst());
        assertTrue(groupSize.getSecond() >= 0f);
        assertTrue(groupSize.getSecond() <= 1f);
    }

    // WHITE BOX TESTING

    @Test
    void enumNull_setsWeightZero() {
        FilterQuery q = new FilterQuery();

        ArrayList<Pair<Integer, Float>> v =
                RoomSearcher.Vectorise(q);

        assertEquals(0, v.get(0).getFirst());
        assertEquals(0f, v.get(0).getSecond());
    }

    @Test
    void enumNonNull_setsWeightOne() {
        FilterQuery q = new FilterQuery();
        q.preferredNoiseLevel = NoiseLevel.QUIET_DISCUSSION;

        var v = RoomSearcher.Vectorise(q);

        assertEquals(1, v.get(0).getFirst());
        assertTrue(v.get(0).getSecond() >= 0f &&
                v.get(0).getSecond() <= 1f);
    }

    /* --------------------------------------------------
       BOOLEAN NORMALISATION BRANCHES
       -------------------------------------------------- */

    @Test
    void booleanNull_weightZero() {
        FilterQuery q = new FilterQuery();

        var v = RoomSearcher.Vectorise(q);

        assertEquals(0, v.get(2).getFirst());
    }

    @Test
    void booleanTrue_encodesOne() {
        FilterQuery q = new FilterQuery();
        q.preferredGroupSpace = true;

        var v = RoomSearcher.Vectorise(q);

        assertEquals(1, v.get(2).getFirst());
        assertEquals(1f, v.get(2).getSecond());
    }

    @Test
    void booleanFalse_encodesZeroValue() {
        FilterQuery q = new FilterQuery();
        q.preferredGroupSpace = false;

        var v = RoomSearcher.Vectorise(q);

        assertEquals(1, v.get(2).getFirst());
        assertEquals(0f, v.get(2).getSecond());
    }

    /* --------------------------------------------------
       AMENITIES BRANCHES
       -------------------------------------------------- */

    @Test
    void nullAmenities_addsEightDummyEntries() {
        FilterQuery q = new FilterQuery();

        var v = RoomSearcher.Vectorise(q);

        for (int i = 3; i < 11; i++) {
            assertEquals(0, v.get(i).getFirst());
            assertEquals(0f, v.get(i).getSecond());
        }
    }

    @Test
    void amenitiesPresent_iteratesAllBooleans() {


        Amenities a = Amenities.builder()
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
                .build();

        FilterQuery q = new FilterQuery();
        q.preferredAmenities = a;

        var v = RoomSearcher.Vectorise(q);

        // first amenity starts at index 3
        assertEquals(1f, v.get(3).getSecond());
        assertEquals(1f, v.get(4).getSecond());
    }

    /* --------------------------------------------------
       GROUP SIZE BRANCHES
       -------------------------------------------------- */

    @Test
    void groupSizeNull_weightZero() {
        FilterQuery q = new FilterQuery();

        var v = RoomSearcher.Vectorise(q);

        Pair<Integer, Float> last = v.get(VECTOR_SIZE - 1);

        assertEquals(0, last.getFirst());
        assertEquals(0f, last.getSecond());
    }

    @Test
    void groupSizeNormalised_correctRange() {
        FilterQuery q = new FilterQuery();
        q.preferredGroupSize = 5;

        var v = RoomSearcher.Vectorise(q);

        Pair<Integer, Float> last = v.get(VECTOR_SIZE - 1);

        assertEquals(1, last.getFirst());
        assertEquals(0.5f, last.getSecond());
    }

    /* --------------------------------------------------
       STRUCTURAL INVARIANT
       -------------------------------------------------- */

    @Test
    void vectorAlwaysSameLength() {
        FilterQuery q = new FilterQuery();

        var v = RoomSearcher.Vectorise(q);

        assertEquals(VECTOR_SIZE, v.size());
    }




}