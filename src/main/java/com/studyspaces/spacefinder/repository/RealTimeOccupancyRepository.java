package com.studyspaces.spacefinder.repository;

import com.studyspaces.spacefinder.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface RealTimeOccupancyRepository
        extends MongoRepository<RoomOccupancyRecord, String> {

    /**
     * Find a record document by its id.
     */
    Optional<RoomOccupancyRecord> findById(String id);

    /**
     * Retrieve all occupancy entries for a given occupancy level.
     */
    @Query("{ 'records.occupancyLevel': ?0 }")
    List<RoomOccupancyRecord> findByOccupancyLevel(Occupancy occupancyLevel);

    /**
     * Retrieve all documents that contain records after a given timestamp.
     */
    @Query("{ 'records.timestamp': { $gt: ?0 } }")
    List<RoomOccupancyRecord> findWithRecordsAfterTimestamp(long timestamp);

    /**
     * Retrieve a specific document and filter its records by timestamp.
     */
    @Query(value = "{ '_id': ?0 }",
           fields = "{ 'records': { $elemMatch: { 'timestamp': { $gt: ?1 } } } }")
    Optional<RoomOccupancyRecord> findRecordsAfterTimestamp(String id, int timestamp);

    /**
     * Retrieves an occupancy record from a specified room document with the most recent timestamp
     */
    @Query(value = "{'_id': ?0}", fields = "{ 'records': { $slice: -1}}")
    Optional<OccupancyRecord> findLastRoomOccupancy(String id);


}



