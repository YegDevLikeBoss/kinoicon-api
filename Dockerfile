# --- Build stage ---
FROM gradle:8.11-jdk21 AS build
WORKDIR /workspace
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean bootJar --no-daemon

# --- Run stage ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar

ENV DB_HOST=db \
    DB_PORT=5432 \
    DB_NAME=kinoicon \
    DB_USER=kinoicon \
    DB_PASSWORD=kinoicon \
    JWT_SECRET=change-this-secret-in-production-min-32-bytes-long \
    JWT_EXPIRATION_MINUTES=60 \
    SEARCH_SIMILARITY_THRESHOLD=0.1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
