# API Endpoints Documentation

**Base URL:** `http://localhost:8080`

## 1. Spaces & Search (`SpaceController`)
| Method | Endpoint | Description                            |
| :--- | :--- |:---------------------------------------|
| `GET` | `/api/spaces` | Get all study spaces                   |
| `GET` | `/api/spaces/{id}` | Get details for a specific space       |
| `GET` | `/api/spaces/search?q={keyword}` | Search by location name or description |
| `GET` | `/api/spaces/filter/noise?level={QUIET/MODERATE}` | Filter by noise level                  |
| `GET` | `/api/spaces/filter/occupancy?level={EMPTY/BUSY}` | Filter by occupancy                    |
| `GET` | `/api/spaces/filter/features?computers=true` | Filter by amenities                    |

## 2. Analytics (`AnalyticsController`)
| Method | Endpoint | Description                    |
| :--- | :--- |:-------------------------------|
| `GET` | `/api/analytics/rooms/summary` | Get summary table of all rooms |
| `GET` | `/api/analytics/buildings` | Get average stats per building |
| `GET` | `/api/analytics/rooms/{id}/peak` | Get the busiest hour for a room |
| `GET` | `/api/analytics/rooms/most-used` | Get list of most popular rooms. |
| `GET` | `/api/analytics/rooms/under-utilised` | Get list of quiet/empty rooms  |

## 3. Authentication (`UserController`)
| Method | Endpoint | Body (JSON) | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/login` | `{ "username": "admin", "password": "password" }` | Logs a user in (Stub). |
| `POST` | `/api/auth/signup` | `{ "username": "...", "password": "..." }` | Registers a new user. |