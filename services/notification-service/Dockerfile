# Multi-stage Dockerfile for notification-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Maven reads all reactor modules even when selecting a single service.
COPY pom.xml ./
COPY services ./services
RUN mvn -B -ntp package -pl services/notification-service -am -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/notification-service/target/*.jar app.jar

EXPOSE 8086

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
