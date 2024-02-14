FROM openjdk:17.0.1-jdk-slim
MAINTAINER sebastien.chevre@jura.ch
COPY target/file-watcher.jar file-watcher.jar
ENTRYPOINT ["java","-jar","/file-watcher.jar"]