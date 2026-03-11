# API Endpoints Documentation

**Base URL:** `http://localhost:8080`

## 1. Spaces & Search (`SpaceController`)
**Base Path:** `/api/spaces`

| Method | Endpoint | Body (JSON) | Returns | Description |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/` | - | `List<StudySpaceProfile>` | Get all study spaces (Retrieval for map pins & details). |
| `GET` | `/{id}` | - | `StudySpaceProfile` | Get details for a specific space. |
| `POST` | `/recommended` | `SearchQueryRequest` | `List<StudySpaceProfile>` | Get top 5 recommendations based on filters. |
| `POST` | `/recommendedSearch`| `SearchQueryRequest` | `SearchResponseDTO` | Advanced search returning exact text matches AND recommendations. |
| `GET` | `/filter/noise?level={QUIET...}` | - | `List<StudySpaceProfile>` | Quick filter by noise level. |
| `GET` | `/filter/occupancy?level={EMPTY...}`| - | `List<StudySpaceProfile>` | Quick filter by live occupancy. |
| `GET` | `/filter/features?computers=true&groups=true` | - | `List<StudySpaceProfile>` | Quick filter by amenities (computers or groups). |

### ⚠️ Frontend Note: Using `/recommendedSearch`
The `POST /api/spaces/recommendedSearch` endpoint returns a specialised DTO object containing **two separate arrays** so you can split the UI into "Exact Matches" and "Similar Recommendations". Duplicate rooms are automatically removed from the recommendations array if they appear in the exact matches.

**Example Response:**
```json
{
  "exactMatches": [
    { "id": "1", "roomLocation": "Quiet Library", "noiseLevel": "QUIET" }
  ],
  "recommendations": [
    { "id": "2", "roomLocation": "Cozy Cafe", "noiseLevel": "MODERATE" },
    { "id": "3", "roomLocation": "Empty Classroom", "noiseLevel": "QUIET" }
  ]
}
```

---

## 2. Analytics (`AnalyticsController`)
**Base Path:** `/api/analytics`

| Method | Endpoint | Returns | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/rooms/summary` | `List<RoomUtilisationDTO>` | Utilisation Page: Get summary table of all rooms. |
| `GET` | `/buildings` | `List<BuildingUtilisationDTO>` | Utilisation Page: Get average stats per building. |
| `GET` | `/rooms/{id}/peak` | `PeakUsageDTO` | Utilisation Page: Get the busiest hour for a specific room. |
| `GET` | `/rooms/most-used` | `List<RoomUtilisationDTO>` | Insights: Get list of the most popular rooms. |
| `GET` | `/rooms/least-used` | `List<RoomUtilisationDTO>` | Insights: Get list of the absolute least used rooms. |
| `GET` | `/rooms/under-utilised` | `List<RoomUtilisationDTO>` | Insights: Get list of quiet/under-utilised rooms. |

---

## 3. Live Occupancy & Check-ins (`OccupancyController`)
**Base Path:** `/checkIn` *(Note: This does not use `/api/` prefix)*

| Method | Endpoint | Body (JSON) | Returns | Description |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/{roomId}/occupancy` | - | `Occupancy` (Enum) | Returns the last reported occupancy status for a room. |
| `GET` | `/{roomId}/last-update` | - | `Long` | Returns the time (in seconds) since the last report was submitted. |
| `GET` | `/{roomId}/average-occupancy` | - | `Occupancy` (Enum) | Returns the average occupancy for a room from the last 7 days. |
| `POST` | `/{roomId}/check-in` | `CheckInDTO` | `String` (Message) | Submit a user check-in report to update live occupancy. |

**Example Check-In Payload (`CheckInDTO`):**
```json
{
  "occupancy": "HIGH" 
}
```
*(Valid Occupancy enums usually include: `LOW`, `MEDIUM`, `HIGH`, `EMPTY`, etc. depending on your Java model).*

---

## 4. Authentication (`UserController`)
**Base Path:** `/api/auth`

| Method | Endpoint | Body (JSON) | Returns | Description |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/login` | `{ "username": "...", "password": "..." }` | `{ "message": "..." }` | Authenticates a user. Returns 200 OK on success, 401 on invalid credentials. |
| `POST` | `/signup` | `{ "username": "...", "password": "..." }` | `{ "message": "..." }` | Registers a new user. Returns 200 OK on success, 400 if username is taken. |