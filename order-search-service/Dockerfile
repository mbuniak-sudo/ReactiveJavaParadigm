# build
FROM gradle:7.6.2-jdk11 as builder
WORKDIR /build
COPY --chown=gradle:gradle src /build/src
COPY --chown=gradle:gradle build.gradle settings.gradle /build/
RUN gradle --no-daemon build

# runtime
FROM eclipse-temurin:11-jre as runtime
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
ENTRYPOINT java -jar app.jar