package com.studyspaces.spacefinder.controller;

import com.studyspaces.spacefinder.dto.BuildingUtilisationDTO;
import com.studyspaces.spacefinder.dto.PeakUsageDTO;
import com.studyspaces.spacefinder.dto.RoomUtilisationDTO;
import com.studyspaces.spacefinder.service.UtilisationAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics") // Base URL: localhost:8080/api/analytics
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final UtilisationAnalyticsService analyticsService;

    public AnalyticsController(UtilisationAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // Utilisation Page: Table of all rooms
    @GetMapping("/rooms/summary")
    public List<RoomUtilisationDTO> getRoomSummary() {
        return analyticsService.getRoomUtilisationSummary();
    }

    // Utilisation Page: Building stats
    @GetMapping("/buildings")
    public List<BuildingUtilisationDTO> getBuildingStats() {
        return analyticsService.getBuildingUtilisationSummary();
    }

    // Utilisation Page: Peak usage for specific room
    @GetMapping("/rooms/{id}/peak")
    public ResponseEntity<PeakUsageDTO> getPeakUsage(@PathVariable String id) {
        PeakUsageDTO dto = analyticsService.getPeakUsageForRoom(id);
        return ResponseEntity.ok(dto);
    }

    // Insights: Most Used list
    @GetMapping("/rooms/most-used")
    public List<RoomUtilisationDTO> getMostUsed() {
        return analyticsService.getMostUsedRooms();
    }

    // Insights: Least Used list
    @GetMapping("/rooms/least-used")
    public List<RoomUtilisationDTO> getLeastUsed() {
        return analyticsService.getLeastUsedRooms();
    }

    // Insights: Under Utilised list
    @GetMapping("/rooms/under-utilised")
    public List<RoomUtilisationDTO> getUnderUtilised() {
        return analyticsService.getUnderUtilisedRooms();
    }
}