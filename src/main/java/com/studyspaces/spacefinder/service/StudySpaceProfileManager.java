package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.model.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

/**
 * StudySpaceProfileManager
 *
 * This service layer handles business logic and provides a clean interface
 * to interact with the StudySpaceRepository.
 */
@Service
public class StudySpaceProfileManager {

    private final StudySpaceRepository repository;

    public StudySpaceProfileManager(StudySpaceRepository repository) {
        this.repository = repository;
    }

    // ===========================
    // CREATE / UPDATE
    // ===========================
    public StudySpaceProfile save(StudySpaceProfile profile) {
        return repository.save(profile);
    }

    /**
     * Replace an existing profile by id.
     * Copies all fields from newProfile except the id.
     */
    public StudySpaceProfile replace(String id, StudySpaceProfile newProfile) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setRoomLocation(newProfile.getRoomLocation());
                    existing.setNotes(newProfile.getNotes());
                    existing.setOccupancy(newProfile.getOccupancy());
                    existing.setNoiseLevel(newProfile.getNoiseLevel());
                    existing.setSuitableForGroups(newProfile.isSuitableForGroups());
                    existing.setMaxGroupSize(newProfile.getMaxGroupSize());
                    existing.setAmenities(newProfile.getAmenities());
                    existing.setSchedule(newProfile.getSchedule());
                    existing.setCoordinates(newProfile.getCoordinates());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
    }

    // ===========================
    // READ
    // ===========================
    public Optional<StudySpaceProfile> getById(String id) {
        return repository.findById(id);
    }

    public List<StudySpaceProfile> getAll() {
        return repository.findAll();
    }

    public List<StudySpaceProfile> getByIds(List<String> ids) { return repository.findByIds(ids);}

    public Optional<StudySpaceProfile> getFirstByRoomLocation(String roomLocation) {
        return repository.findFirstByRoomLocation(roomLocation);
    }

    public List<StudySpaceProfile> getByRoomLocation(String roomLocation) {
        return repository.findByRoomLocation(roomLocation);
    }

    public List<StudySpaceProfile> searchByRoomLocationKeyword(String keyword) {
        return repository.findByRoomLocationContaining(keyword);
    }

    public List<StudySpaceProfile> getByOccupancy(Occupancy occupancy) {
        return repository.findByOccupancy(occupancy);
    }

    public List<StudySpaceProfile> getByOccupancyIn(List<Occupancy> occupancies) {
        return repository.findByOccupancyIn(occupancies);
    }

    public List<StudySpaceProfile> getByNoiseLevel(NoiseLevel noiseLevel) {
        return repository.findByNoiseLevel(noiseLevel);
    }

    public List<StudySpaceProfile> getByNoiseLevelIn(List<NoiseLevel> noiseLevels) {
        return repository.findByNoiseLevelIn(noiseLevels);
    }

    public List<StudySpaceProfile> getSuitableForGroups() {
        return repository.findBySuitableForGroupsTrue();
    }

    public List<StudySpaceProfile> getNotSuitableForGroups() {
        return repository.findBySuitableForGroupsFalse();
    }

    public List<StudySpaceProfile> getByMaxGroupSizeGreaterThan(int size) {
        return repository.findByMaxGroupSizeGreaterThan(size);
    }

    public List<StudySpaceProfile> getByMaxGroupSizeLessThanEqual(int size) {
        return repository.findByMaxGroupSizeLessThanEqual(size);
    }

    // ===========================
    // Amenities helpers
    // ===========================
    public List<StudySpaceProfile> getWithPlugSockets() {
        return repository.findByAmenitiesPlugSocketsTrue();
    }

    public List<StudySpaceProfile> getWithDesks() {
        return repository.findByAmenitiesDesksTrue();
    }

    public List<StudySpaceProfile> getWithComputers() {
        return repository.findByAmenitiesComputersTrue();
    }

    public List<StudySpaceProfile> getWithPrinters() {
        return repository.findByAmenitiesPrintersTrue();
    }

    public List<StudySpaceProfile> getWithFoodAllowed() {
        return repository.findByAmenitiesFoodAllowedTrue();
    }

    public List<StudySpaceProfile> getWithToiletNearby() {
        return repository.findByAmenitiesToiletNearbyTrue();
    }

    public List<StudySpaceProfile> getWheelchairAccessible() {
        return repository.findByAmenitiesWheelchairAccessibleTrue();
    }

    public List<StudySpaceProfile> getWithSocketsAndDesks() {
        return repository.findByAmenitiesPlugSocketsTrueAndAmenitiesDesksTrue();
    }

    // ===========================
    // Schedule helpers
    // ===========================
    public List<StudySpaceProfile> getByScheduleDay(int day) {
        return repository.findByScheduleDay(day);
    }

    public List<StudySpaceProfile> getByScheduleDayAndTimeAfter(int day, int time) {
        return repository.findByScheduleDayAndScheduleTimeGreaterThan(day, time);
    }

    // ===========================
    // Notes / search
    // ===========================
    public List<StudySpaceProfile> searchNotes(String keyword) {
        return repository.findByNotesContaining(keyword);
    }

    // ===========================
    // DELETE
    // ===========================
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public void deleteByRoomLocation(String roomLocation) {
        repository.deleteByRoomLocation(roomLocation);
    }

    public void deleteByRoomLocationKeyword(String keyword) {
        repository.deleteByRoomLocationContaining(keyword);
    }
}
