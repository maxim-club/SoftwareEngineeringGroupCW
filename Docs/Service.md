# Service Layer Documentation

<details>
<summary> StudySpaceProfileManager</summary>
Overview

StudySpaceProfileManager is the service layer responsible for handling business logic related to StudySpaceProfile entities.

It acts as an intermediary between controllers and the database repository, ensuring clean separation of concerns.

Responsibilities:

- Manage creation and updates of study space profiles

- Provide filtered search operations

- Handle domain-specific queries

- Delegate persistence operations to StudySpaceRepository

### Package
```java
com.studyspaces.spacefinder.service
```
<details>
<summary>StudySpaceProfileManager Methods</summary>

| Category                | Method                           | Parameters                                | Returns                       | Description                                              |
| ----------------------- | -------------------------------- | ----------------------------------------- | ----------------------------- | -------------------------------------------------------- |
| **Create / Update**     | `save`                           | `StudySpaceProfile profile`               | `StudySpaceProfile`           | Saves or updates a study space profile                   |
|                         | `replace`                        | `String id, StudySpaceProfile newProfile` | `StudySpaceProfile`           | Replaces all fields of an existing profile except the ID |
| **Read**                | `getById`                        | `String id`                               | `Optional<StudySpaceProfile>` | Retrieves a profile by ID                                |
|                         | `getAll`                         | —                                         | `List<StudySpaceProfile>`     | Retrieves all study space profiles                       |
|                         | `getFirstByRoomLocation`         | `String roomLocation`                     | `Optional<StudySpaceProfile>` | Returns first profile matching location                  |
|                         | `getByRoomLocation`              | `String roomLocation`                     | `List<StudySpaceProfile>`     | Retrieves profiles by exact room location                |
|                         | `searchByRoomLocationKeyword`    | `String keyword`                          | `List<StudySpaceProfile>`     | Searches room locations using partial match              |
| **Occupancy Filters**   | `getByOccupancy`                 | `Occupancy occupancy`                     | `List<StudySpaceProfile>`     | Filters profiles by occupancy level                      |
|                         | `getByOccupancyIn`               | `List<Occupancy> occupancies`             | `List<StudySpaceProfile>`     | Filters by multiple occupancy values                     |
| **Noise Level Filters** | `getByNoiseLevel`                | `NoiseLevel noiseLevel`                   | `List<StudySpaceProfile>`     | Filters profiles by noise level                          |
|                         | `getByNoiseLevelIn`              | `List<NoiseLevel> noiseLevels`            | `List<StudySpaceProfile>`     | Filters by multiple noise levels                         |
| **Group Suitability**   | `getSuitableForGroups`           | —                                         | `List<StudySpaceProfile>`     | Returns spaces suitable for groups                       |
|                         | `getNotSuitableForGroups`        | —                                         | `List<StudySpaceProfile>`     | Returns spaces not suitable for groups                   |
|                         | `getByMaxGroupSizeGreaterThan`   | `int size`                                | `List<StudySpaceProfile>`     | Finds spaces supporting groups larger than given size    |
|                         | `getByMaxGroupSizeLessThanEqual` | `int size`                                | `List<StudySpaceProfile>`     | Finds spaces supporting groups up to given size          |
| **Amenities**           | `getWithPlugSockets`             | —                                         | `List<StudySpaceProfile>`     | Spaces with plug sockets                                 |
|                         | `getWithDesks`                   | —                                         | `List<StudySpaceProfile>`     | Spaces with desks                                        |
|                         | `getWithComputers`               | —                                         | `List<StudySpaceProfile>`     | Spaces with computers                                    |
|                         | `getWithPrinters`                | —                                         | `List<StudySpaceProfile>`     | Spaces with printers                                     |
|                         | `getWithFoodAllowed`             | —                                         | `List<StudySpaceProfile>`     | Spaces where food is allowed                             |
|                         | `getWithToiletNearby`            | —                                         | `List<StudySpaceProfile>`     | Spaces with nearby toilets                               |
|                         | `getWheelchairAccessible`        | —                                         | `List<StudySpaceProfile>`     | Wheelchair accessible spaces                             |
|                         | `getWithSocketsAndDesks`         | —                                         | `List<StudySpaceProfile>`     | Spaces requiring both sockets AND desks                  |
| **Schedule**            | `getByScheduleDay`               | `int day`                                 | `List<StudySpaceProfile>`     | Returns spaces available on a specific day               |
|                         | `getByScheduleDayAndTimeAfter`   | `int day, int time`                       | `List<StudySpaceProfile>`     | Returns spaces open after a given time                   |
| **Notes Search**        | `searchNotes`                    | `String keyword`                          | `List<StudySpaceProfile>`     | Searches keyword within notes field                      |
| **Delete**              | `deleteById`                     | `String id`                               | `void`                        | Deletes profile by ID                                    |
|                         | `deleteByRoomLocation`           | `String roomLocation`                     | `void`                        | Deletes profiles by exact location                       |
|                         | `deleteByRoomLocationKeyword`    | `String keyword`                          | `void`                        | Deletes profiles matching location keyword               |

</details>
</details>