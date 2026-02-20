FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

#RUN mkdir /app

COPY pom.xml .
COPY src ./src

#RUN mvn clean install -DskipTests
RUN --mount=type=cache,target=/root/.m2 mvn clean install  -DskipTests

FROM eclipse-temurin:21 AS final
#FROM openjdk:21-jdk-slim DEPRECATED SOMEHOW

WORKDIR /app
#RUN mkdir /app2

COPY --from=build /app/target/*.jar marchcat-storage.jar

CMD ["java", "-jar", "marchcat-storage.jar"]
