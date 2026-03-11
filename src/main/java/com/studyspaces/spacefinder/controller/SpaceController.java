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

@RestController
@RequestMapping("/api/spaces")
@CrossOrigin(origins = "*")
public class SpaceController {

    private final StudySpaceProfileManager profileManager;

    public SpaceController(StudySpaceProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    // RETRIEVAL (map pins & space details)

    @GetMapping
    public List<StudySpaceProfile> getAllSpaces() {
        System.out.println(">>> getAllSpaces() called");
        List<StudySpaceProfile> spaces = profileManager.getAll();
        System.out.println(">>> spaces returned: " + spaces.size());
        return spaces;
    }

//    @GetMapping
//    public List<StudySpaceProfile> getAllSpaces() {
//        return profileManager.getAll();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<StudySpaceProfile> getSpaceById(@PathVariable String id) {
        return profileManager.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ADVANCED SEARCH & RECOMMENDATION

    @PostMapping("/recommended")
    public List<StudySpaceProfile> getRecommendedSpaces(@RequestBody SearchQueryRequest request) {
        List<String> ids = RoomSearcher.getKRecommended(request.getFilters(), 5);
        return profileManager.getByIds(ids);
    }

    @PostMapping("/recommendedSearch")
    public SearchResponseDTO getRecommendedSpacesWithSearch(@RequestBody SearchQueryRequest request) {
        // extract raw query
        String rawQuery = request.getSearchBarBuildingQuery();
        String searchTerm = (rawQuery != null) ? rawQuery.toLowerCase() : "";

        List<StudySpaceProfile> exactMatches = new ArrayList<>();

        // 2. find exact matches (if search term provided)
        if (!searchTerm.isBlank()) {
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

        // recommendations
        List<String> ids = RoomSearcher.getSortedByRecommended(request.getFilters());
        List<StudySpaceProfile> recommendations = profileManager.getByIds(ids);

        // remove duplicates
        final List<StudySpaceProfile> finalExactMatches = exactMatches;
        recommendations = recommendations.stream()
                .filter(rec -> !finalExactMatches.contains(rec))
                .toList();

        return new SearchResponseDTO(exactMatches, recommendations);
    }

    // QUICK FILTERS
    // old code, can be removed if not used

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