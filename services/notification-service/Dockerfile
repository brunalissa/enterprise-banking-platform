# Multi-stage Dockerfile for notification-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/notification-service/pom.xml ./services/notification-service/
RUN mvn dependency:go-offline -pl services/notification-service -am || true

# Copy source and build
COPY services/notification-service/src ./services/notification-service/src
COPY services/notification-service/pom.xml ./services/notification-service/
RUN mvn package -pl services/notification-service -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/notification-service/target/*.jar app.jar

EXPOSE 8086

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
