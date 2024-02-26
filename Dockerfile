FROM maven:3.9.5-eclipse-temurin-17-focal AS MAVEN_BUILD

COPY pom.xml /build/
COPY src /build/src/

WORKDIR /build/
RUN mvn package

FROM openjdk:17.0.1-jdk-slim
RUN groupadd -g 1000 defaultuser \
  && useradd --create-home --no-log-init -u 1000 -g 1000 defaultuser
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/file-watcher.jar /app/
USER defaultuser
ENTRYPOINT ["java", "-jar", "file-watcher.jar"]

