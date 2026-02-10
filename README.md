# SoftwareEngineeringGroupCW



Frontend to Backend connection:
1. Run 'npm start'
2. Run SpacefinderApplication
3. Navigate to 'http://localhost:3000/backendtest'


















## Study Space Profile
### StudySpaceProfile

| Category     | Field Name          | Type                  | Description                                                                 |
| ------------ | ------------------- | --------------------- | --------------------------------------------------------------------------- |
| Identifier   | `id`                | `String`              | Unique identifier for the study space profile (database primary key).       |
| Core Info    | `roomLocation`      | `String`              | Human-readable location of the study space (e.g. building and room number). |
| Core Info    | `notes`             | `String`              | Free-text notes describing the study space.                                 |
| Status       | `occupancy`         | `Occupancy`           | Current or typical occupancy level of the space.                            |
| Status       | `noiseLevel`        | `NoiseLevel`          | Typical noise level of the study space.                                     |
| Groups       | `suitableForGroups` | `boolean`             | Indicates whether the space is suitable for group study.                    |
| Groups       | `maxGroupSize`      | `Integer`             | Maximum recommended group size (null if not applicable).                    |
| Facilities   | `amenities`         | `Amenities`           | Available facilities such as desks, plug sockets, printers, etc.            |
| Time + Place | `schedule`          | `List<ScheduleEntry>` | Availability schedule entries for the study space.                          |
| Time + Place | `coordinates`       | `Coordinates`         | Physical coordinates used for mapping or location services.                 |



### StudySpaceProfileManager

| Category        | Method Name                      | Parameters                                  | Return Type                   | Description                                                                                         |
| --------------- | -------------------------------- | ------------------------------------------- | ----------------------------- | --------------------------------------------------------------------------------------------------- |
| Create / Update | `save`                           | `StudySpaceProfile profile`                 | `StudySpaceProfile`           | Saves a new profile or updates an existing one.                                                     |
| Create / Update | `replace`                        | `String id`, `StudySpaceProfile newProfile` | `StudySpaceProfile`           | Replaces all fields of an existing profile **except the ID**. Throws if the profile does not exist. |
| Read            | `getById`                        | `String id`                                 | `Optional<StudySpaceProfile>` | Retrieves a profile by its unique ID.                                                               |
| Read            | `getAll`                         | none                                        | `List<StudySpaceProfile>`     | Returns all study space profiles.                                                                   |
| Read            | `getFirstByRoomLocation`         | `String roomLocation`                       | `Optional<StudySpaceProfile>` | Returns the first profile matching a room location.                                                 |
| Read            | `getByRoomLocation`              | `String roomLocation`                       | `List<StudySpaceProfile>`     | Retrieves all profiles for a specific room location.                                                |
| Read            | `searchByRoomLocationKeyword`    | `String keyword`                            | `List<StudySpaceProfile>`     | Finds profiles whose room location contains the given keyword.                                      |
| Read            | `getByOccupancy`                 | `Occupancy occupancy`                       | `List<StudySpaceProfile>`     | Retrieves profiles by occupancy level.                                                              |
| Read            | `getByOccupancyIn`               | `List<Occupancy> occupancies`               | `List<StudySpaceProfile>`     | Retrieves profiles matching any of the given occupancy values.                                      |
| Read            | `getByNoiseLevel`                | `NoiseLevel noiseLevel`                     | `List<StudySpaceProfile>`     | Retrieves profiles by noise level.                                                                  |
| Read            | `getByNoiseLevelIn`              | `List<NoiseLevel> noiseLevels`              | `List<StudySpaceProfile>`     | Retrieves profiles matching any of the given noise levels.                                          |
| Read            | `getSuitableForGroups`           | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles suitable for group study.                                                        |
| Read            | `getNotSuitableForGroups`        | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles not suitable for group study.                                                    |
| Read            | `getByMaxGroupSizeGreaterThan`   | `int size`                                  | `List<StudySpaceProfile>`     | Finds profiles allowing group sizes greater than the given value.                                   |
| Read            | `getByMaxGroupSizeLessThanEqual` | `int size`                                  | `List<StudySpaceProfile>`     | Finds profiles allowing group sizes up to and including the given value.                            |
| Amenities       | `getWithPlugSockets`             | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles with plug sockets available.                                                     |
| Amenities       | `getWithDesks`                   | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles with desks available.                                                            |
| Amenities       | `getWithComputers`               | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles with computers available.                                                        |
| Amenities       | `getWithPrinters`                | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles with printers available.                                                         |
| Amenities       | `getWithFoodAllowed`             | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles where food is allowed.                                                           |
| Amenities       | `getWithToiletNearby`            | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles with nearby toilets.                                                             |
| Amenities       | `getWheelchairAccessible`        | none                                        | `List<StudySpaceProfile>`     | Retrieves wheelchair-accessible study spaces.                                                       |
| Amenities       | `getWithSocketsAndDesks`         | none                                        | `List<StudySpaceProfile>`     | Retrieves profiles with both plug sockets and desks.                                                |
| Schedule        | `getByScheduleDay`               | `int day`                                   | `List<StudySpaceProfile>`     | Retrieves profiles available on a specific day.                                                     |
| Schedule        | `getByScheduleDayAndTimeAfter`   | `int day`, `int time`                       | `List<StudySpaceProfile>`     | Retrieves profiles available after a given time on a specific day.                                  |
| Search          | `searchNotes`                    | `String keyword`                            | `List<StudySpaceProfile>`     | Searches profile notes for the given keyword.                                                       |
| Delete          | `deleteById`                     | `String id`                                 | `void`                        | Deletes a profile by its ID.                                                                        |
| Delete          | `deleteByRoomLocation`           | `String roomLocation`                       | `void`                        | Deletes all profiles with the specified room location.                                              |
| Delete          | `deleteByRoomLocationKeyword`    | `String keyword`                            | `void`                        | Deletes profiles whose room location contains the given keyword.                                    |
	

##Database Models

####Realtime Occupancy

id: String
records: [ 
    Timestamp: int,
    occupancyLevel: Enum,
]

####Study Space


