package com.studyspaces.ControllerTests;

import com.studyspaces.spacefinder.SpacefinderApplication;
import com.studyspaces.spacefinder.controller.AnalyticsController;
import com.studyspaces.spacefinder.dto.BuildingUtilisationDTO;
import com.studyspaces.spacefinder.dto.PeakUsageDTO;
import com.studyspaces.spacefinder.dto.RoomUtilisationDTO;
import com.studyspaces.spacefinder.service.UtilisationAnalyticsService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
@ContextConfiguration(classes = SpacefinderApplication.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisationAnalyticsService analyticsService;

    @Test
    void shouldReturnRoomSummary() throws Exception {

        List<RoomUtilisationDTO> rooms = List.of(
                new RoomUtilisationDTO("R1", "Library", 0.75, false, null),
                new RoomUtilisationDTO("R2", "Science", 0.55, false, null)
        );

        when(analyticsService.getRoomUtilisationSummary()).thenReturn(rooms);

        mockMvc.perform(get("/api/analytics/rooms/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomId").value("R1"))
                .andExpect(jsonPath("$[0].roomLocation").value("Library"))
                .andExpect(jsonPath("$[0].utilisationPercent").value(75.0));

        verify(analyticsService).getRoomUtilisationSummary();
    }

    @Test
    void shouldReturnBuildingStats() throws Exception {

        List<BuildingUtilisationDTO> buildings =
                List.of(new BuildingUtilisationDTO("Library", 0.7));

        when(analyticsService.getBuildingUtilisationSummary()).thenReturn(buildings);

        mockMvc.perform(get("/api/analytics/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].building").value("Library"))
                .andExpect(jsonPath("$[0].utilisationPercent").value(70.0));

        verify(analyticsService).getBuildingUtilisationSummary();
    }

    @Test
    void shouldReturnPeakUsageForRoom() throws Exception {

        PeakUsageDTO peak = new PeakUsageDTO(
                "R101",
                List.of(10, 11),
                List.of("Monday", "Tuesday"),
                null
        );

        when(analyticsService.getPeakUsageForRoom("R101")).thenReturn(peak);

        mockMvc.perform(get("/api/analytics/rooms/R101/peak"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").value("R101"))
                .andExpect(jsonPath("$.busiestTimes[0]").value(10))
                .andExpect(jsonPath("$.busiestDays[0]").value("Monday"));

        verify(analyticsService).getPeakUsageForRoom("R101");
    }

    @Test
    void shouldReturnMostUsedRooms() throws Exception {

        List<RoomUtilisationDTO> rooms =
                List.of(new RoomUtilisationDTO("R10", "Library", 0.95, false, null));

        when(analyticsService.getMostUsedRooms()).thenReturn(rooms);

        mockMvc.perform(get("/api/analytics/rooms/most-used"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomId").value("R10"))
                .andExpect(jsonPath("$[0].utilisationPercent").value(95.0));

        verify(analyticsService).getMostUsedRooms();
    }

    @Test
    void shouldReturnLeastUsedRooms() throws Exception {

        List<RoomUtilisationDTO> rooms =
                List.of(new RoomUtilisationDTO("R50", "Engineering", 0.10, true, null));

        when(analyticsService.getLeastUsedRooms()).thenReturn(rooms);

        mockMvc.perform(get("/api/analytics/rooms/least-used"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomId").value("R50"))
                .andExpect(jsonPath("$[0].underUtilised").value(true));

        verify(analyticsService).getLeastUsedRooms();
    }

    @Test
    void shouldReturnUnderUtilisedRooms() throws Exception {

        List<RoomUtilisationDTO> rooms =
                List.of(new RoomUtilisationDTO("R77", "Maths", 0.20, true, null));

        when(analyticsService.getUnderUtilisedRooms()).thenReturn(rooms);

        mockMvc.perform(get("/api/analytics/rooms/under-utilised"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomId").value("R77"))
                .andExpect(jsonPath("$[0].underUtilised").value(true));

        verify(analyticsService).getUnderUtilisedRooms();
    }
}