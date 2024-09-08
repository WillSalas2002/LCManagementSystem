FROM openjdk:11-jdk

WORKDIR /app

COPY target/rest-crud-api-0.0.1-SNAPSHOT.jar /app/rest-crud-api.jar

EXPOSE 8080

CMD ["java", "-jar", "rest-crud-api.jar"]