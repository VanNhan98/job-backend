FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} job-backend.jar

ENTRYPOINT ["java", "-jar", "job-backend.jar"]

EXPOSE 8080