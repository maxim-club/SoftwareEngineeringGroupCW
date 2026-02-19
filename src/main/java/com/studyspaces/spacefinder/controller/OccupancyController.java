package com.studyspaces.spacefinder.controller;

import com.studyspaces.spacefinder.dto.CheckInDTO;
import com.studyspaces.spacefinder.model.CheckInReport;
import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.service.OccupancyManager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkIn")
public class OccupancyController {

    private final OccupancyManager occupancyManager;

    public OccupancyController(OccupancyManager occupancyManager) {
        this.occupancyManager = occupancyManager;
    }

    // ===========================
    // READ
    // ===========================

    // Returns the last reported occupancy for a room
    @GetMapping("/{roomId}/occupancy")
    public ResponseEntity<Occupancy> getLastOccupancy(@PathVariable String roomId) {

        Occupancy occupancy = occupancyManager.getLastOccupancy(roomId);

        if (occupancy == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(occupancy);
    }

    // Returns the time (in seconds) since the last report was submitted for a room
    @GetMapping("/{roomId}/last-update")
    public ResponseEntity<Long> getLastUpdate(@PathVariable String roomId) {

        long seconds = occupancyManager.whenLastOccupancyWasAdded(roomId);

        if (seconds == -1) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(seconds);
    }

    // ===========================
    // READ
    // ===========================

    @PostMapping("/{roomId}/check-in")
    public ResponseEntity<String> userCheckIn(
            @PathVariable String roomId,
            @RequestBody CheckInDTO dto
    ) {

        CheckInReport report = occupancyManager.toModel(dto);
        boolean success = occupancyManager.userCheckIn(roomId, report);

        if (!success) {
            return ResponseEntity.badRequest().body("Invalid room or report");
        }

        return ResponseEntity.ok("Check-in successful");
    }
}
