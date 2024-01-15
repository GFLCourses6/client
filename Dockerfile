# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build

COPY . .
RUN mvn clean package -DskipTests -Dmaven

FROM openjdk:17-slim

COPY --from=build /target/client-0.0.1-SNAPSHOT.jar client.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","client.jar"]
