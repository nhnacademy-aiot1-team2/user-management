FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ./target/management-0.0.1-SNAPSHOT.jar TxT-management.jar
ENTRYPOINT ["java", "-jar", "/TxT-management.jar"]