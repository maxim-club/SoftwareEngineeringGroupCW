package com.studyspaces.spacefinder.controller;

import com.studyspaces.spacefinder.dto.SearchQueryRequest;
import com.studyspaces.spacefinder.dto.SearchResponseDTO;
import com.studyspaces.spacefinder.model.NoiseLevel;
import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.StudySpaceProfile;
import com.studyspaces.spacefinder.service.RoomSearcher;
import com.studyspaces.spacefinder.service.StudySpaceProfileManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping
    public List<StudySpaceProfile> getAllSpaces() {
        return profileManager.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudySpaceProfile> getSpaceById(@PathVariable String id) {
        return profileManager.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // RECOMMENDATION & ADVANCED SEARCH

    @PostMapping("/recommended")
    public List<StudySpaceProfile> getRecommendedSpaces(@RequestBody SearchQueryRequest request){
        List<String> ids = RoomSearcher.getKRecommended(request.getFilters(), 5);
        return profileManager.getByIds(ids);
    }

    @PostMapping("/recommendedSearch")
    public SearchResponseDTO getRecommendedSpacesWithSearch(@RequestBody SearchQueryRequest request){
        String rawQuery = request.getSearchBarBuildingQuery();
        String searchTerm = (rawQuery != null) ? rawQuery.toLowerCase() : "";

        List<StudySpaceProfile> exactMatches = new ArrayList<>();

        // find exact matches
        if (!searchTerm.isEmpty()) {
            List<StudySpaceProfile> allRooms = profileManager.getAll();
            exactMatches = allRooms.stream()
                    .filter(room -> {
                        boolean matchesLocation = room.getRoomLocation() != null &&
                                room.getRoomLocation().toLowerCase().contains(searchTerm);
                        boolean matchesNotes = room.getNotes() != null &&
                                room.getNotes().toLowerCase().contains(searchTerm);
                        return matchesLocation || matchesNotes;
                    })
                    .toList();
        }

        // get recommendations
        List<String> ids = RoomSearcher.getSortedByRecommended(request.getFilters());
        List<StudySpaceProfile> recommendations = profileManager.getByIds(ids);

        // remove duplicates
        List<StudySpaceProfile> finalExactMatches = exactMatches; // Need a final variable for the stream
        recommendations = recommendations.stream()
                .filter(rec -> !finalExactMatches.contains(rec))
                .toList();

        // package into DTO
        return new SearchResponseDTO(exactMatches, recommendations);
    }

    // OLD TEXT SEARCH & FILTERS
    // can be deleted if not used

    @GetMapping("/search")
    public List<StudySpaceProfile> searchByKeyword(@RequestParam String q) {
        List<StudySpaceProfile> byLocation = profileManager.searchByRoomLocationKeyword(q);
        List<StudySpaceProfile> byNotes = profileManager.searchNotes(q);

        return Stream.concat(byLocation.stream(), byNotes.stream())
                .filter(distinctByKey(StudySpaceProfile::getId))
                .collect(Collectors.toList());
    }

    private static <T> java.util.function.Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Set<Object> seen = java.util.concurrent.ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @GetMapping("/filter/noise")
    public List<StudySpaceProfile> getByNoise(@RequestParam NoiseLevel level) {
        return profileManager.getByNoiseLevel(level);
    }

    @GetMapping("/filter/occupancy")
    public List<StudySpaceProfile> getByOccupancy(@RequestParam Occupancy level) {
        return profileManager.getByOccupancy(level);
    }

    @GetMapping("/filter/features")
    public List<StudySpaceProfile> getByFeatures(
            @RequestParam(required = false) boolean computers,
            @RequestParam(required = false) boolean groups) {

        if (computers) return profileManager.getWithComputers();
        if (groups) return profileManager.getSuitableForGroups();

        return profileManager.getAll();
    }
}