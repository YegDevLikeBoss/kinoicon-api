# Kinoicon API (Spring Boot)

Rewrite of the original Flask + MongoDB project onto Java 21 / Spring Boot / PostgreSQL / plain
JDBC, keeping the external API contract identical to the original OpenAPI spec.

## Stack
- Java 21, Gradle
- Spring Boot (Web, JDBC, Validation)
- springdoc-openapi (Swagger UI at `/swagger-ui`, spec at `/v3/api-docs`)
- PostgreSQL 16, plain SQL via `JdbcTemplate` (no ORM)
- JWT auth via `jjwt`, no server-side session object anywhere
- Lombok

## Running locally

### 1. Database
```bash
docker run --name kinoicon-db -e POSTGRES_DB=kinoicon -e POSTGRES_USER=kinoicon \
  -e POSTGRES_PASSWORD=kinoicon -p 5432:5432 -d postgres:16-alpine
psql -h localhost -U kinoicon -d kinoicon -f src/main/resources/db.sql
```

### 2. First admin user
There is no open sign-up: `POST /register` itself requires a valid JWT (mirrors the original
Flask behaviour). Insert the first account directly:
```sql
-- password hash below is a BCrypt(12) hash - generate your own with any bcrypt tool
INSERT INTO app_user (username, password, rights)
VALUES ('admin', '$2b$12$replace-with-a-real-bcrypt-hash', ARRAY['ADMIN']);
```

### 3. Run the app
```bash
./gradlew bootRun
# or
docker compose up --build
```

## Everything with Docker Compose
```bash
docker compose up --build
```
This starts Postgres (auto-running `db.sql` on first init) and the API on `:8080`.

## Known gaps / follow-ups worth knowing about
- `UserRight` (`USER`/`ADMIN`) is stored but **not enforced** anywhere yet - this was an
  explicit "placeholder for later" decision, matching the original project.
- The Gradle wrapper jar itself isn't included (no network access to generate one in this
  environment) - run `gradle wrapper` once with a local Gradle install, or use your own
  Gradle/IDE to import the project directly.
- The project was authored without a working JDK/compiler in this environment, so it has
  **not been compiled or run** here - it's been reviewed by hand for consistency, but budget
  a first `./gradlew build` pass to catch anything that slipped through.
- `FilmSummaryResponse.director` / `.mainActors` are derived by scanning `film_crew` roles for
  the strings "director" / "actor" (case-insensitive) - the original Mongo aggregation logic
  that computed these `readOnly` fields wasn't available, so this is a best-effort port; adjust
  the matching rule in `FilmServiceImpl` if your role vocabulary differs.
- pg_trgm search only indexes the `name` fields (en/ru/native), as requested - not logline.
