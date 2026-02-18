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

<details>
<summary> RoomSearcher </summary>

## Overview

`RoomSearcher` is a service responsible for performing **vector-based similarity search** over study spaces.

It converts study space attributes and user filter preferences into normalized numeric vectors and applies a **K-Nearest Neighbours (KNN)** algorithm to return the most relevant room recommendations.

The service maintains an **in-memory search space** built from all study space profiles stored in the database, allowing fast recommendation queries without repeated database access.

### Responsibilities

- Load and cache all study spaces into memory
- Convert filter preferences into normalized vectors
- Perform weighted similarity comparison
- Return top-K recommended study spaces
- Provide testing access to cached search data

---

### Package

```java
com.studyspaces.spacefinder.service
```

<details>
<summary>RoomSearcher Public Methods</summary>

<br>

| Category                | Method                  | Parameters                        | Returns                                       | Description                                                                                                                                                       |
| ----------------------- | ----------------------- | --------------------------------- | --------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Initialisation**      | `RoomSearcher`          | `StudySpaceRepository repository` | —                                             | Constructor that injects the repository used to fetch study space data.                                                                                           |
|                         | `initialiseSearchSpace` | —                                 | `void`                                        | Loads all study spaces from the database, vectorises their filter data, and stores them in an in-memory search map for fast querying.                             |
| **Testing / Debugging** | `getSearchSpace`        | —                                 | `HashMap<String, List<Pair<Integer, Float>>>` | Returns the current cached search space. Primarily intended for testing and verification.                                                                         |
| **Vectorisation**       | `Vectorise`             | `FilterQuery query`               | `ArrayList<Pair<Integer, Float>>`             | Converts a user filter query into a fixed-length normalized vector. Each element contains a weight (0 or 1) and a value between 0–1 representing user preference. |
| **Recommendation**      | `getKRecommended`       | `FilterQuery query, int k`        | `List<String>`                                | Performs a KNN similarity search and returns the IDs of the `k` closest matching study spaces ordered from most similar to least similar.                         |

</details>

Design Notes
Vector Representation

Each vector element is stored as:

```java
Pair<Integer, Float>
```

Integer (weight)

- 1 → parameter influences similarity

- 0 → parameter ignored (user indifferent)

Float (value)

- Normalized preference value in range 0–1

This allows dimensions to be selectively excluded during distance calculation.

Time Complexity
```
O(N log K)
```
Where:

- N = number of study spaces
- K = requested recommendations

</details>