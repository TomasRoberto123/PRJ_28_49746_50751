#FROM openjdk:15-jdk-alpine
#FROM openjdk
FROM docker.io/library/openjdk:15-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/museu-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]