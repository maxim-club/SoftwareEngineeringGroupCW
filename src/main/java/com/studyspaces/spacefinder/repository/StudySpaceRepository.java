package com.studyspaces.spacefinder.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

import com.studyspaces.spacefinder.model.*;
/**
 * StudySpaceRepository
 * 
 * This repository provides auto-generated query methods for StudySpaceProfile.
 * Spring Data MongoDB automatically implements these methods based on method names.
 */

@Repository
public interface StudySpaceRepository extends MongoRepository<StudySpaceProfile, String> {

    // ===========================
    // CRUD / Core
    // ===========================
    Optional<StudySpaceProfile> findById(String id);
    List<StudySpaceProfile> findAll();
    void deleteById(String id);

    // ===========================
    // Room Location
    // ===========================
    Optional<StudySpaceProfile> findFirstByRoomLocation(String roomLocation);
    List<StudySpaceProfile> findByRoomLocation(String roomLocation);
    List<StudySpaceProfile> findByRoomLocationContaining(String keyword);
    void deleteByRoomLocation(String roomLocation);

    // ===========================
    // Occupancy
    // ===========================
    List<StudySpaceProfile> findByOccupancy(Occupancy occupancy);
    List<StudySpaceProfile> findByOccupancyIn(List<Occupancy> occupancies);

    // ===========================
    // Noise Level
    // ===========================
    List<StudySpaceProfile> findByNoiseLevel(NoiseLevel noiseLevel);
    List<StudySpaceProfile> findByNoiseLevelIn(List<NoiseLevel> noiseLevels);

    // ===========================
    // Group suitability
    // ===========================
    List<StudySpaceProfile> findBySuitableForGroupsTrue();
    List<StudySpaceProfile> findBySuitableForGroupsFalse();
    List<StudySpaceProfile> findByMaxGroupSizeGreaterThan(int size);
    List<StudySpaceProfile> findByMaxGroupSizeLessThanEqual(int size);
    List<StudySpaceProfile> findBySuitableForGroupsTrueAndMaxGroupSizeGreaterThanEqual(int size);

    // ===========================
    // Amenities (booleans)
    // ===========================
    List<StudySpaceProfile> findByAmenitiesPlugSocketsTrue();
    List<StudySpaceProfile> findByAmenitiesDesksTrue();
    List<StudySpaceProfile> findByAmenitiesComputersTrue();
    List<StudySpaceProfile> findByAmenitiesPrintersTrue();
    List<StudySpaceProfile> findByAmenitiesFoodAllowedTrue();
    List<StudySpaceProfile> findByAmenitiesToiletNearbyTrue();
    List<StudySpaceProfile> findByAmenitiesWheelchairAccessibleTrue();
    List<StudySpaceProfile> findByAmenitiesPlugSocketsTrueAndAmenitiesDesksTrue();

    // ===========================
    // Schedule
    // ===========================
	@Query("{ 'schedule.day': ?0 }")
    List<StudySpaceProfile> findByScheduleDay(int day);
	@Query("{ 'schedule.day': ?0, 'schedule.time': { $gt: ?1 } }")
    List<StudySpaceProfile> findByScheduleDayAndScheduleTimeGreaterThan(int day, int time);

    // ===========================
    // Notes / Text search
    // ===========================
    List<StudySpaceProfile> findByNotesContaining(String keyword);


    // ===========================
    // Delete helpers
    // ===========================
    void deleteByRoomLocationContaining(String keyword);
}


