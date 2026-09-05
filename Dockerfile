# --- Build stage ---
FROM gradle:8.9-jdk17 AS build
WORKDIR /app

# Copia primeiro apenas os arquivos de build para aproveitar o cache de camadas do Docker
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts ./
COPY --chown=gradle:gradle gradle ./gradle
RUN gradle dependencies --no-daemon || true

COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

# --- Run stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
