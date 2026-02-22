FROM gradle:jdk21-alpine AS build
WORKDIR /build
COPY gradle gradle
COPY gradlew settings.gradle build.gradle ./
RUN ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew assemble --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
COPY --from=build /build/build/libs/*.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
