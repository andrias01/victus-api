FROM amazoncorretto:23-alpine-jdk

COPY target/victus-api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java" , "-jar" , "/app.jar"]