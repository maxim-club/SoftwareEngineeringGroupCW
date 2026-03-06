package com.studyspaces.ControllerTests;

import com.studyspaces.spacefinder.SpacefinderApplication;
import com.studyspaces.spacefinder.controller.OccupancyController;
import com.studyspaces.spacefinder.dto.CheckInDTO;
import com.studyspaces.spacefinder.model.CheckInReport;
import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.service.OccupancyManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OccupancyController.class)
@ContextConfiguration(classes = SpacefinderApplication.class)
class OccupancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OccupancyManager occupancyManager;

    // ===========================
    // GET /{roomId}/occupancy
    // ===========================
    @Test
    void shouldReturnLastOccupancy() throws Exception {
        Occupancy occupancy = Occupancy.MEDIUM;
        when(occupancyManager.getLastOccupancy("R101")).thenReturn(occupancy);

        mockMvc.perform(get("/checkIn/R101/occupancy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("MEDIUM"));

        verify(occupancyManager).getLastOccupancy("R101");
    }

    @Test
    void shouldReturnNotFoundWhenNoLastOccupancy() throws Exception {
        when(occupancyManager.getLastOccupancy("R999")).thenReturn(null);

        mockMvc.perform(get("/checkIn/R999/occupancy"))
                .andExpect(status().isNotFound());

        verify(occupancyManager).getLastOccupancy("R999");
    }

    // ===========================
    // GET /{roomId}/last-update
    // ===========================
    @Test
    void shouldReturnLastUpdateSeconds() throws Exception {
        when(occupancyManager.whenLastOccupancyWasAdded("R101")).thenReturn(120L);

        mockMvc.perform(get("/checkIn/R101/last-update"))
                .andExpect(status().isOk())
                .andExpect(content().string("120"));

        verify(occupancyManager).whenLastOccupancyWasAdded("R101");
    }

    @Test
    void shouldReturnNotFoundForLastUpdate() throws Exception {
        when(occupancyManager.whenLastOccupancyWasAdded("R999")).thenReturn(-1L);

        mockMvc.perform(get("/checkIn/R999/last-update"))
                .andExpect(status().isNotFound());

        verify(occupancyManager).whenLastOccupancyWasAdded("R999");
    }

    // ===========================
    // GET /{roomId}/average-occupancy
    // ===========================
    @Test
    void shouldReturn7DayAverage() throws Exception {
        Occupancy occupancy = Occupancy.LOW;
        when(occupancyManager.get7DayAverage("R101")).thenReturn(occupancy);

        mockMvc.perform(get("/checkIn/R101/average-occupancy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("LOW"));

        verify(occupancyManager).get7DayAverage("R101");
    }

    @Test
    void shouldReturnNotFoundFor7DayAverage() throws Exception {
        when(occupancyManager.get7DayAverage("R999")).thenReturn(null);

        mockMvc.perform(get("/checkIn/R999/average-occupancy"))
                .andExpect(status().isNotFound());

        verify(occupancyManager).get7DayAverage("R999");
    }

    // ===========================
    // POST /{roomId}/check-in
    // ===========================
    @Test
    void shouldAcceptUserCheckIn() throws Exception {
        CheckInDTO dto = new CheckInDTO("HIGH");
        CheckInReport report = new CheckInReport(Occupancy.HIGH);

        when(occupancyManager.toModel(dto)).thenReturn(report);
        when(occupancyManager.userCheckIn("R101", report)).thenReturn(true);

        mockMvc.perform(post("/checkIn/R101/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"closed\":true,\"wifiIssue\":false,\"reserved\":false,\"fullyOccupied\":false,\"occupancy\":\"HIGH\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Check-in successful"));

        verify(occupancyManager).toModel(dto);
        verify(occupancyManager).userCheckIn("R101", report);
    }

    @Test
    void shouldRejectInvalidCheckIn() throws Exception {
        CheckInDTO dto = new CheckInDTO("LOW");
        CheckInReport report = new CheckInReport(Occupancy.LOW);

        when(occupancyManager.toModel(dto)).thenReturn(report);
        when(occupancyManager.userCheckIn("R999", report)).thenReturn(false);

        mockMvc.perform(post("/checkIn/R999/check-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"closed\":false,\"wifiIssue\":false,\"reserved\":true,\"fullyOccupied\":false,\"occupancy\":\"LOW\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid room or report"));

        verify(occupancyManager).toModel(dto);
        verify(occupancyManager).userCheckIn("R999", report);
    }
}
