FROM maven:3.9.5-eclipse-temurin-17-focal AS MAVEN_BUILD

MAINTAINER Brian Hannaway

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

COPY --from=MAVEN_BUILD /build/target/file-watcher.jar /app/

ENTRYPOINT ["java", "-jar", "file-watcher.jar"]

