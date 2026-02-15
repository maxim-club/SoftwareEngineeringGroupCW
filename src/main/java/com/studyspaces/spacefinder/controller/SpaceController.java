package com.studyspaces.spacefinder.controller;

import com.studyspaces.spacefinder.model.NoiseLevel;
import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.service.StudySpaceProfileManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spaces") // Base URL: localhost:8080/api/spaces
@CrossOrigin(origins = "*")
public class SpaceController {

    private final StudySpaceProfileManager profileManager;

    public SpaceController(StudySpaceProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    // HOME PAGE & MAP

    // Get all spaces (for map pins)
    @GetMapping
    public List<StudySpaceProfile> getAllSpaces() {
        return profileManager.getAll();
    }

    // Get specific space details (when clicking a pin or result)
    @GetMapping("/{id}")
    public ResponseEntity<StudySpaceProfile> getSpaceById(@PathVariable String id) {
        return profileManager.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // SEARCH & FILTER PAGE

    // Text Search (Location name / Notes)
    @GetMapping("/search")
    public List<StudySpaceProfile> searchByKeyword(@RequestParam String q) {
        List<StudySpaceProfile> byLocation = profileManager.searchByRoomLocationKeyword(q);
        List<StudySpaceProfile> byNotes = profileManager.searchNotes(q);

        // Combine both lists to remove duplicates when result matches both location and notes
        return Stream.concat(byLocation.stream(), byNotes.stream())
                .filter(distinctByKey(StudySpaceProfile::getId)) // Custom filter helper
                .collect(Collectors.toList());
    }

    // Helper method to filter by a specific key (ID)
    private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Set<Object> seen = java.util.concurrent.ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    // Filter by Noise Level (e.g. /api/spaces/filter/noise?level=QUIET)
    @GetMapping("/filter/noise")
    public List<StudySpaceProfile> getByNoise(@RequestParam NoiseLevel level) {
        return profileManager.getByNoiseLevel(level);
    }

    // Filter by Occupancy (e.g. /api/spaces/filter/occupancy?level=EMPTY)
    @GetMapping("/filter/occupancy")
    public List<StudySpaceProfile> getByOccupancy(@RequestParam Occupancy level) {
        return profileManager.getByOccupancy(level);
    }

    // Filters by features (e.g. /api/spaces/filter/features?computers=true)
    @GetMapping("/filter/features")
    public List<StudySpaceProfile> getByFeatures(
            @RequestParam(required = false) boolean computers,
            @RequestParam(required = false) boolean groups) {

        if (computers) return profileManager.getWithComputers();
        if (groups) return profileManager.getSuitableForGroups();

        return profileManager.getAll();
    }
}