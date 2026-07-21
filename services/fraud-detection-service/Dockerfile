# Multi-stage Dockerfile for fraud-detection-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/fraud-detection-service/pom.xml ./services/fraud-detection-service/
RUN mvn dependency:go-offline -pl services/fraud-detection-service -am || true

# Copy source and build
COPY services/fraud-detection-service/src ./services/fraud-detection-service/src
COPY services/fraud-detection-service/pom.xml ./services/fraud-detection-service/
RUN mvn package -pl services/fraud-detection-service -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/fraud-detection-service/target/*.jar app.jar

EXPOSE 8087

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
