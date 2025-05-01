FROM openjdk:21

WORKDIR /app

COPY target/target/scm2.0-0.0.1-SNAPSHOT.jar /app/target/scm2.0-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "scm2.0-0.0.1-SNAPSHOT.jar"]

