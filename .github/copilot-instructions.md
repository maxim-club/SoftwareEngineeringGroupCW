# Copilot / AI agent instructions for StudySpace

Purpose: help AI coding agents become productive quickly in this repository.

1) Big picture
- Backend: Spring Boot service in [src/main/java/com/studyspaces/spacefinder](src/main/java/com/studyspaces/spacefinder). Controllers expose a REST API under `/api/*` (see [SpaceController.java](src/main/java/com/studyspaces/spacefinder/controller/SpaceController.java), [OccupancyController.java](src/main/java/com/studyspaces/spacefinder/controller/OccupancyController.java), [UserController.java](src/main/java/com/studyspaces/spacefinder/controller/UserController.java)).
- Frontend: React app in `frontend/` (entry: [frontend/src/App.js](frontend/src/App.js)). Routes use `react-router-dom` and the `LayoutWithNavbar` wrapper for most pages.
- Data store: MongoDB. Backend uses Spring Data MongoDB and tests use embedded Mongo (flapdoodle).

2) How components communicate
- Frontend calls backend HTTP endpoints (examples in [frontend/src/services/apiServices.js](frontend/src/services/apiServices.js)). Primary endpoints:
  - `GET /api/spaces` — all spaces
  - `GET /api/spaces/{id}` — space details
  - `POST /api/checkIn/{roomId}/check-in` — submit a `CheckInDTO` (see [dto/CheckInDTO.java](src/main/java/com/studyspaces/spacefinder/dto/CheckInDTO.java))
  - `POST /api/spaces/recommended` and `/recommendedSearch` — recommendation flows using `RoomSearcher` utility.

3) Developer workflows & commands
- Start backend (dev): `./gradlew bootRun` (project root). API serves on `http://localhost:8080`. See [README.md](README.md).
- Run backend tests: `./gradlew test`.
- Frontend (dev): `cd frontend && npm install && npm start` (dev server on :3000). `package.json` contains `start`, `build`, `test` scripts.
- Build frontend for production: `cd frontend && npm run build`.

4) Project-specific patterns & gotchas
- Constructor injection for services: service/manager classes are injected into controllers via constructors (no `@Autowired` fields). Examples: `StudySpaceProfileManager` in `SpaceController`.
- Manager classes pattern: classes in `service/` (e.g., `OccupancyManager`, `StudySpaceProfileManager`) encapsulate domain logic; controllers delegate to them.
- Recommendation/search utilities are implemented as static helpers (see `RoomSearcher` in `service/`). Prefer reading these helpers when modifying recommendation logic.
- DTOs live under `dto/` and map to request/response payloads. Models live under `model/` (e.g., `StudySpaceProfile`, `Occupancy`).
- CORS: controllers annotated with `@CrossOrigin(origins = "*")` — dev-friendly open CORS.

5) Integration & environment details
- MongoDB is required for full backend runs. Tests use embedded MongoDB (flapdoodle). Check `build.gradle` dependencies.
- The backend includes `io.github.cdimascio:dotenv-java` — environment variables may be loaded from `.env` in some environments.
- Frontend `package.json` sets `proxy: "http://localhost:8080"`, but `frontend/src/services/apiServices.js` uses a hardcoded `API_BASE_URL = 'http://localhost:8080'`. Both exist; be careful when changing base URLs.

6) Build/CI considerations
- Gradle Java toolchain in `build.gradle` specifies a Java version. README recommends Java 17+; confirm local JDK compatibility before running builds.
- Tests rely on JUnit 5 and Mockito; run `./gradlew test` to reproduce CI test runs.

7) Quick examples for modifications
- Add a new endpoint: create controller in `controller/`, delegate to a `service/` manager and add model/dto under `model/` or `dto/`.
- Update frontend call: modify `frontend/src/services/apiServices.js` or add a new API helper and call it from the page under `frontend/src/pages/`.

8) Where to look for more documentation
- API surface: [Docs/API_Endpoints.md](Docs/API_Endpoints.md).
- Models & services: [Docs/Models.md](Docs/Models.md) and [Docs/Service.md](Docs/Service.md).

If anything here is unclear or you want this file to include additional examples (curl snippets, common PR tasks, or preferred commit messages), tell me which sections to expand.
