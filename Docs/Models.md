## Model Layer Documentation

### Classes

<details>
<summary><strong>Amenities</strong></summary>

## Amenities Class

The `Amenities` class represents the facilities available within a study space.  
It is used as a data model to store boolean flags describing what resources or accessibility options are present.

---

### Package
```java
com.studyspaces.spacefinder.model
```
### Fields
| Field                  | Type      | Description                                           |
| ---------------------- | --------- | ----------------------------------------------------- |
| `plugSockets`          | `boolean` | Indicates if plug sockets are available.              |
| `desks`                | `boolean` | Indicates if desks are present.                       |
| `computers`            | `boolean` | Indicates if computers are available for use.         |
| `printers`             | `boolean` | Indicates if printing facilities are available.       |
| `foodAllowed`          | `boolean` | Specifies whether food consumption is permitted.      |
| `waterFountainNearby`  | `boolean` | Indicates if a water fountain is nearby.              |
| `toiletNearby`         | `boolean` | Indicates if toilets are located nearby.              |
| `wheelchairAccessible` | `boolean` | Specifies whether the space is wheelchair accessible. |
</details>




<details>
<summary><strong>Coordinates</strong></summary>

## Coordinates Class

The `Coordinates` class represents a single latitude & longitude coordinate

---

### Package
```java
com.studyspaces.spacefinder.model
```
### Fields
| Field       | Type     |
|-------------|----------|
| `latitude`  | `double` |
| `longitude` | `double` |

</details>





<details>
<summary> Filter Query </summary>
  
## FilterQuery

Represents a user's study space preferences used when generating recommendations.

A `null` value indicates **no preference (indifference)** for that attribute during similarity comparison.

---

### Package
`com.studyspaces.spacefinder.model`

---

<details>
<summary><strong>Fields</strong></summary>

<br>

| Field | Type | Description |
|------|------|-------------|
| `preferredNoiseLevel` | `NoiseLevel` | Desired environmental noise level (e.g., quiet or social). |
| `preferredOccupancy` | `Occupancy` | Preferred crowd density of the study space. |
| `preferredAmenities` | `Amenities` | Requested available amenities (e.g., sockets, Wi-Fi, facilities). |
| `preferredGroupSpace` | `Boolean` | Indicates whether the user prefers a group study area. |
| `preferredGroupSize` | `Integer` | Preferred group size capacity for the study space. |

</details>

---

### Notes

- Fields may be `null`, meaning the user has no preference.
- Used as input for vectorisation and KNN recommendation search.
- Lombok annotations generate getters, setters, constructors, and utility methods automatically.

</details>




<details>
<summary><strong>OccupancyRecord</strong></summary>

## OccupancyRecord Class

The `OccupancyRecord` class represents a single historical record of a study space’s occupancy at a specific point in time.  
It is typically used for tracking and analysing occupancy trends.

---

### 📦 Package
```java
com.studyspaces.spacefinder.model
```

No-Arguments Constructor
```java
public OccupancyRecord() {}
```
Used for framework deserialization and object creation.

Parameterized Constructor
```jave
public OccupancyRecord(long timestamp, Occupancy occupancyLevel, Boolean closed, Boolean wifiIssue, Boolean reserved, Boolean fullyOccupied) {
        this.timestamp = timestamp;
        this.occupancyLevel = occupancyLevel;
        this.closed = closed;
        this.wifiIssue = wifiIssue;
        this.reserved = reserved;
        this.fullyOccupied = fullyOccupied;
    }
```

Creates a record with a specific timestamp and occupancy level.

| Field            | Type        | Description                                                                                     |
|------------------|-------------|-------------------------------------------------------------------------------------------------|
| `timestamp`      | `long`      | The recorded time of the occupancy measurement (typically Unix timestamp or system time value). |
| `occupancyLevel` | `Occupancy` | The occupancy state of the study space at the recorded time.                                    |
| `closed`         | `Boolean`   | Whether the study space is closed                                                               |
| `wifiIssue`      | `Boolean`   | Whether the study space has wifiIssues                                                          |
| `reserved`       | `Boolean`   | Whether the study space is reserved                                                             |
| `fullyOccupied`  | `Boolean`   | Whether the study space is fully occupied                                                       |

</details>

<details>
<summary><strong>CheckInReport</strong></summary>

## CheckInReport Class

The `CheckInReport` class represents the data submitted by a user when checking into a study space.  
It captures the user’s perception of the room’s current condition and occupancy status at the time of check-in.

---

### Package
```java
com.studyspaces.spacefinder.model
```

---

### Purpose

This model is used to:

- Collect user-submitted room status information
- Transfer validated check-in data to the service layer
- Generate a corresponding `OccupancyRecord`
- Update real-time occupancy tracking

It acts as a domain model representation of a check-in submission.

---

### Fields

| Field | Type | Description |
|-------|------|-------------|
| `closed` | `Boolean` | Indicates whether the study space is closed at the time of check-in. |
| `wifiIssue` | `Boolean` | Indicates if there are Wi-Fi connectivity problems. |
| `reserved` | `Boolean` | Indicates whether the space is reserved. |
| `fullyOccupied` | `Boolean` | Indicates whether all seating capacity appears to be taken. |
| `occupancy` | `Occupancy` | The user-selected occupancy level (`EMPTY`, `FREE`, `MODERATE`, `BUSY`). |

---

### Constructors

#### All-Arguments Constructor
```java
public CheckInReport(Boolean closed, Boolean wifiIssue, Boolean reserved,
                     Boolean fullyOccupied, Occupancy occupancy)
```
Creates a complete check-in report with all status fields defined.

#### No-Arguments Constructor
```java
public CheckInReport() {}
```
Required for:
- JSON deserialization
- Framework object instantiation
- MongoDB/Spring mapping compatibility

---

### Notes

- All boolean fields use `Boolean` (object type) rather than `boolean` to allow potential null handling.
- Designed as a lightweight domain object focused purely on check-in state reporting.
- Does not contain timestamp information — timestamps are generated when creating `OccupancyRecord`.

</details>


<details>
<summary><strong>RoomOccupancyRecord</strong></summary>

## RoomOccupancyRecord Class

The `RoomOccupancyRecord` class represents a MongoDB document that stores
the occupancy history for a specific study space or room.  
It groups multiple `OccupancyRecord` entries under a single room identifier.

---

### 📦 Package
```java
com.studyspaces.spacefinder.model
```

---

###  MongoDB Mapping

| Annotation | Description |
|------------|-------------|
| `@Document(collection = "occupancy_records")` | Maps this class to the `occupancy_records` MongoDB collection. |
| `@Id` | Marks the field used as the unique document identifier. |

---

### Fields

| Field | Type | Description |
|------|------|-------------|
| `id` | `String` | Unique identifier for the room or study space document. |
| `records` | `List<OccupancyRecord>` | List of historical occupancy entries associated with the room. |

---
</details>

<details>
<summary><strong>ScheduleEntry</strong></summary>

##  ScheduleEntry Class

The `ScheduleEntry` class represents a single opening-hours entry for a study space.  
It defines the day of the week and the hours during which the space is open.

---

### 📦 Package
```java
com.studyspaces.spacefinder.model
```

---

### Fields

| Field | Type | Description |
|------|------|-------------|
| `day` | `int` | Day of the week (`1 = Monday`, `7 = Sunday`). |
| `openHour` | `int` | Opening hour in 24-hour format (`0–23`). |
| `closeHour` | `int` | Closing hour in 24-hour format (`0–23`). |

---

</details>



<details>
<summary><strong>StudySpaceProfile </strong></summary>

## StudySpaceProfile Class

---

### 📦 Package
```java
com.studyspaces.spacefinder.model
```

---

### 🧱 Fields

| Field | Type | Description |
|------|------|-------------|
| `id` | `String` | Unique identifier for the study space document. |
| `roomLocation` | `String` | Name or description of the room location. |
| `notes` | `String` | Additional information or comments about the space. |
| `occupancy` | `Occupancy` | Current occupancy level of the study space. |
| `noiseLevel` | `NoiseLevel` | Expected noise environment of the space. |
| `suitableForGroups` | `boolean` | Indicates whether the space is suitable for group study. |
| `maxGroupSize` | `Integer` | Maximum recommended group size. |
| `amenities` | `Amenities` | Available facilities and accessibility features. |
| `schedule` | `List<ScheduleEntry>` | Opening hours schedule for the study space. |
| `coordinates` | `Coordinates` | Geographic location of the study space. |

---

### 🔧 Methods

*(Generated automatically by Lombok `@Data`)*

| Method Type | Description |
|-------------|-------------|
| Getters | Retrieve values for all fields. |
| Setters | Update values for all fields. |
| `toString()` | Returns string representation of the object. |
| `equals()` / `hashCode()` | Provides object comparison and hashing behaviour. |

Additional constructors:
- `StudySpaceProfile()` — No-arguments constructor for framework deserialization.
- All-arguments constructor generated by `@AllArgsConstructor`.

</details>

### Enums
<details>
<summary><strong>NoiseLevel Enum</strong></summary>

## NoiseLevel Enum

The `NoiseLevel` enum defines the expected sound environment within a study space.  
It is used to categorise locations based on how much noise is acceptable.

---

### 📦 Package
```java
com.studyspaces.spacefinder.model;
```
| Value              | Description                                                   |
| ------------------ | ------------------------------------------------------------- |
| `SILENT`           | Very quiet environment intended for focused individual study. |
| `QUIET_DISCUSSION` | Low noise level where quiet conversations are allowed.        |
| `MODERATE`         | Normal background noise typical of shared study areas.        |
| `LOUD`             | High noise environment suitable for social or group activity. |

</details>

<details>
<summary><strong>Occupancy Enum</strong></summary>

##  Occupancy Enum

The `Occupancy` enum represents how crowded a study space currently is.  
It helps users understand availability before choosing a location.

---

### 📦 Package
```java
com.studyspaces.spacefinder.model
```
| Value      | Description                                     |
| ---------- | ----------------------------------------------- |
| `EMPTY`    | No occupants present.                           |
| `FREE`     | Mostly available with plenty of seating.        |
| `MODERATE` | Partially occupied with limited free space.     |
| `BUSY`     | Highly occupied with little or no availability. |


</details>
