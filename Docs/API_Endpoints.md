# API Endpoints Documentation

**Base URL:** `http://localhost:8080`

## 1. Spaces & Search (`SpaceController`)

| Method | Endpoint | Body (JSON) | Returns | Description                                                      |
| :--- | :--- | :--- | :--- |:-----------------------------------------------------------------|
| `GET` | `/api/spaces` | - | `List` | Get all study spaces                                             |
| `GET` | `/api/spaces/{id}` | - | `Object` | Get details for a specific space                                 |
| `GET` | `/api/spaces/search?q={keyword}` | - | `List` | Search by location name or notes                                 |
| `GET` | `/api/spaces/filter/noise?level={QUIET...}` | - | `List` | Filter by noise level                                            |
| `GET` | `/api/spaces/filter/occupancy?level={EMPTY...}` | - | `List` | Filter by live occupancy                                         |
| `GET` | `/api/spaces/filter/features?computers=true` | - | `List` | Filter by amenities                                              |
| `POST` | `/api/spaces/recommended` | `SearchQueryRequest` | `List` | Get recommendations based on filters                             |
| `POST` | `/api/spaces/recommendedSearch`| `SearchQueryRequest` | `SearchResponseDTO` | Advanced search returning exact text matches AND recommendations |

### ⚠️ Frontend Note: Using `/recommendedSearch`
The `POST /api/spaces/recommendedSearch` endpoint returns a specialised DTO object containing **two separate arrays** so you can split the UI into "Exact Matches" and "Similar Recommendations".

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

## 2. Analytics (`AnalyticsController`)
| Method | Endpoint | Description                     |
| :--- | :--- |:--------------------------------|
| `GET` | `/api/analytics/rooms/summary` | Get summary table of all rooms  |
| `GET` | `/api/analytics/buildings` | Get average stats per building  |
| `GET` | `/api/analytics/rooms/{id}/peak` | Get the busiest hour for a room |
| `GET` | `/api/analytics/rooms/most-used` | Get list of most popular rooms  |
| `GET` | `/api/analytics/rooms/under-utilised` | Get list of quiet/empty rooms   |

## 3. Authentication (`UserController`)
| Method | Endpoint | Body (JSON) | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | `{ "username": "admin", "password": "password" }` | Logs a user in (Stub). |
| `POST` | `/api/auth/signup` | `{ "username": "...", "password": "..." }` | Registers a new user. |