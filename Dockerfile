FROM maven:3-openjdk-18-slim AS build-env
COPY . .
RUN mvn package -DskipTests -f pom.xml

FROM openjdk:18-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY --from=build-env ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
