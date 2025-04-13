FROM openjdk:11-jre-slim
WORKDIR /app

COPY ./target/employee-service-0.0.1-SNAPSHOT.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "myapp.jar"]

