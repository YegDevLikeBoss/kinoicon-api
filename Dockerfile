FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY build.gradle* settings.gradle* gradle.properties* ./
RUN gradle --no-daemon dependencies > /dev/null 2>&1 || true
COPY src ./src
RUN gradle --no-daemon bootJar -x test \
 && cp "$(ls build/libs/*.jar | grep -v -- '-plain' | head -n1)" /app/app.jar

FROM eclipse-temurin:21-jre
RUN useradd -r -u 1001 app
WORKDIR /app
COPY --from=build /app/app.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]