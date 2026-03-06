package com.studyspaces.ControllerTests;

import com.studyspaces.spacefinder.SpacefinderApplication;
import com.studyspaces.spacefinder.controller.SpaceController;
import com.studyspaces.spacefinder.dto.SearchQueryRequest;
import com.studyspaces.spacefinder.dto.SearchResponseDTO;
import com.studyspaces.spacefinder.model.NoiseLevel;
import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.model.FilterQuery;
import com.studyspaces.spacefinder.service.StudySpaceProfileManager;
import com.studyspaces.spacefinder.service.RoomSearcher;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpaceController.class)
@ContextConfiguration(classes = SpacefinderApplication.class)
class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudySpaceProfileManager profileManager;

    // ===========================
    // GET /api/spaces
    // ===========================
    @Test
    void shouldReturnAllSpaces() throws Exception {
        StudySpaceProfile s1 = new StudySpaceProfile("1", "Library A", "Address1", "Quiet room",
                Occupancy.LOW, NoiseLevel.QUIET, null, true, 6, null, null, null);
        StudySpaceProfile s2 = new StudySpaceProfile("2", "Library B", "Address2", "Group study",
                Occupancy.MEDIUM, NoiseLevel.LOUD, null, true, 10, null, null, null);

        when(profileManager.getAll()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/spaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));

        verify(profileManager).getAll();
    }

    // ===========================
    // GET /api/spaces/{id}
    // ===========================
    @Test
    void shouldReturnSpaceById() throws Exception {
        StudySpaceProfile space = new StudySpaceProfile("1", "Library A", "Address1", "Quiet room",
                Occupancy.LOW, NoiseLevel.QUIET, null, true, 6, null, null, null);

        when(profileManager.getById("1")).thenReturn(Optional.of(space));

        mockMvc.perform(get("/api/spaces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.roomLocation").value("Library A"));

        verify(profileManager).getById("1");
    }

    @Test
    void shouldReturnNotFoundForInvalidId() throws Exception {
        when(profileManager.getById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/spaces/999"))
                .andExpect(status().isNotFound());

        verify(profileManager).getById("999");
    }

    // ===========================
    // POST /api/spaces/recommended
    // ===========================
    @Test
    void shouldReturnRecommendedSpaces() throws Exception {
        FilterQuery filters = new FilterQuery(null, null, null, null, null);
        SearchQueryRequest request = new SearchQueryRequest();
        request.setFilters(filters);

        StudySpaceProfile s1 = new StudySpaceProfile("1", "Library A", null, null, null, null, null, false, null, null, null, null);
        StudySpaceProfile s2 = new StudySpaceProfile("2", "Library B", null, null, null, null, null, false, null, null, null, null);

        try (MockedStatic<RoomSearcher> roomSearcherMock = mockStatic(RoomSearcher.class)) {
            roomSearcherMock.when(() -> RoomSearcher.getKRecommended(filters, 5)).thenReturn(List.of("1", "2"));
            when(profileManager.getByIds(List.of("1", "2"))).thenReturn(List.of(s1, s2));

            mockMvc.perform(post("/api/spaces/recommended")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"filters\":{}}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value("1"))
                    .andExpect(jsonPath("$[1].id").value("2"));

            verify(profileManager).getByIds(List.of("1", "2"));
        }
    }

    // ===========================
    // POST /api/spaces/recommendedSearch
    // ===========================
    @Test
    void shouldReturnEmptyExactAndRecommendationsWhenNoQueryAndNoRecommendations() throws Exception {
        try (MockedStatic<RoomSearcher> roomSearcherMock = mockStatic(RoomSearcher.class)) {
            roomSearcherMock.when(() -> RoomSearcher.getSortedByRecommended(null))
                    .thenReturn(List.of());
            when(profileManager.getAll()).thenReturn(List.of());
            when(profileManager.getByIds(List.of())).thenReturn(List.of());

            mockMvc.perform(post("/api/spaces/recommendedSearch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"searchBarBuildingQuery\":null,\"filters\":{}}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exactMatches").isEmpty())
                    .andExpect(jsonPath("$.recommendations").isEmpty());
        }
    }

    @Test
    void shouldReturnExactMatchesOnly() throws Exception {
        StudySpaceProfile exact = new StudySpaceProfile("1", "Library A", null, "Quiet study", null, null, null, false, null, null, null, null);
        when(profileManager.getAll()).thenReturn(List.of(exact));

        try (MockedStatic<RoomSearcher> roomSearcherMock = mockStatic(RoomSearcher.class)) {
            roomSearcherMock.when(() -> RoomSearcher.getSortedByRecommended(null))
                    .thenReturn(List.of());

            when(profileManager.getByIds(List.of())).thenReturn(List.of());

            mockMvc.perform(post("/api/spaces/recommendedSearch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"searchBarBuildingQuery\":\"library a\",\"filters\":{}}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exactMatches[0].id").value("1"))
                    .andExpect(jsonPath("$.recommendations").isEmpty());
        }
    }

    @Test
    void shouldReturnRecommendationsOnly() throws Exception {
        // Recommendation profile
        StudySpaceProfile recommendation = new StudySpaceProfile(
                "2", "Library B", null, "Group study",
                null, null, null, false, null, null, null, null
        );

        // exactMatches empty
        when(profileManager.getAll()).thenReturn(List.of());

        // Proper empty FilterQuery to avoid null issues
        com.studyspaces.spacefinder.model.FilterQuery filters =
                new com.studyspaces.spacefinder.model.FilterQuery(null, null, null, null, null);

        SearchQueryRequest request = new SearchQueryRequest();
        request.setFilters(filters);
        request.setSearchBarBuildingQuery("");

        // Mock RoomSearcher static
        try (MockedStatic<RoomSearcher> roomSearcherMock = mockStatic(RoomSearcher.class)) {
            roomSearcherMock.when(() -> RoomSearcher.getSortedByRecommended(filters))
                    .thenReturn(List.of("2"));

            // profileManager returns the recommendation object
            when(profileManager.getByIds(List.of("2"))).thenReturn(List.of(recommendation));

            // Perform request
            mockMvc.perform(post("/api/spaces/recommendedSearch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"searchBarBuildingQuery\":\"\",\"filters\":{}}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exactMatches").isEmpty())
                    .andExpect(jsonPath("$.recommendations[0].id").value("2"))
                    .andExpect(jsonPath("$.recommendations[0].roomLocation").value("Library B"));
        }
    }

    @Test
    void shouldReturnExactAndRecommendationWithoutDuplication() throws Exception {
        // Exact match
        StudySpaceProfile exactMatch = new StudySpaceProfile(
                "1", "Library A", "Address1", "Quiet room",
                null, null, null, false, null, null, null, null);

        // Recommendation (distinct from exact match)
        StudySpaceProfile recommendation = new StudySpaceProfile(
                "2", "Library B", "Address2", "Group study",
                null, null, null, false, null, null, null, null);

        com.studyspaces.spacefinder.model.FilterQuery filters =
                new com.studyspaces.spacefinder.model.FilterQuery(null, null, null, null, null);

        SearchQueryRequest request = new SearchQueryRequest();
        request.setSearchBarBuildingQuery("library");
        request.setFilters(filters);

        // Mock exact matches from search term
        when(profileManager.getAll()).thenReturn(List.of(exactMatch));

        // Mock recommendations
        try (MockedStatic<RoomSearcher> roomSearcherMock = mockStatic(RoomSearcher.class)) {
            roomSearcherMock.when(() -> RoomSearcher.getSortedByRecommended(filters))
                    .thenReturn(List.of("2"));

            when(profileManager.getByIds(List.of("2"))).thenReturn(List.of(recommendation));

            mockMvc.perform(post("/api/spaces/recommendedSearch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"searchBarBuildingQuery\":\"library\",\"filters\":{}}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exactMatches[0].id").value("1"))
                    .andExpect(jsonPath("$.recommendations[0].id").value("2"));
        }
    }

    @Test
    void shouldReturnSearchResponseWithNoSearchTerm() throws Exception {
        when(profileManager.getByIds(List.of("1", "2"))).thenReturn(List.of());

        try (MockedStatic<RoomSearcher> roomSearcherMock = mockStatic(RoomSearcher.class)) {
            roomSearcherMock.when(() -> RoomSearcher.getSortedByRecommended(null)).thenReturn(List.of("1", "2"));

            mockMvc.perform(post("/api/spaces/recommendedSearch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"filters\":{}}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.exactMatches").isEmpty())
                    .andExpect(jsonPath("$.recommendations").isEmpty());
        }
    }

    // ===========================
    // GET /api/spaces/filter/noise
    // ===========================
    @Test
    void shouldReturnSpacesByNoiseLevel() throws Exception {
        StudySpaceProfile space = new StudySpaceProfile("1", "Library A", null, null, null, NoiseLevel.QUIET, null, false, null, null, null, null);
        when(profileManager.getByNoiseLevel(NoiseLevel.QUIET)).thenReturn(List.of(space));

        mockMvc.perform(get("/api/spaces/filter/noise").param("level", "QUIET"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(profileManager).getByNoiseLevel(NoiseLevel.QUIET);
    }

    // ===========================
    // GET /api/spaces/filter/occupancy
    // ===========================
    @Test
    void shouldReturnSpacesByOccupancy() throws Exception {
        StudySpaceProfile space = new StudySpaceProfile("1", "Library A", null, null, Occupancy.LOW, null, null, false, null, null, null, null);
        when(profileManager.getByOccupancy(Occupancy.LOW)).thenReturn(List.of(space));

        mockMvc.perform(get("/api/spaces/filter/occupancy").param("level", "LOW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(profileManager).getByOccupancy(Occupancy.LOW);
    }

    // ===========================
    // GET /api/spaces/filter/features
    // ===========================
    @Test
    void shouldReturnSpacesWithComputers() throws Exception {
        StudySpaceProfile space = new StudySpaceProfile("1", "Library A", null, null, null, null, null, false, null, null, null, null);
        when(profileManager.getWithComputers()).thenReturn(List.of(space));

        mockMvc.perform(get("/api/spaces/filter/features").param("computers", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(profileManager).getWithComputers();
    }

    @Test
    void shouldReturnSpacesForGroups() throws Exception {
        StudySpaceProfile space = new StudySpaceProfile("2", "Library B", null, null, null, null, null, true, null, null, null, null);
        when(profileManager.getSuitableForGroups()).thenReturn(List.of(space));

        mockMvc.perform(get("/api/spaces/filter/features").param("groups", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("2"));

        verify(profileManager).getSuitableForGroups();
    }

    @Test
    void shouldReturnAllSpacesWhenNoFilters() throws Exception {
        StudySpaceProfile s1 = new StudySpaceProfile("1", "Library A", null, null, null, null, null, false, null, null, null, null);
        when(profileManager.getAll()).thenReturn(List.of(s1));

        mockMvc.perform(get("/api/spaces/filter/features"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));

        verify(profileManager).getAll();
    }

    // ===========================
    // Edge case: empty lists
    // ===========================
    @Test
    void shouldReturnEmptyListWhenNoSpacesFound() throws Exception {
        when(profileManager.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/spaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(profileManager).getAll();
    }
}