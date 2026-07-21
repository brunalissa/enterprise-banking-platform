# Multi-stage Dockerfile for api-gateway
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/api-gateway/pom.xml ./services/api-gateway/
RUN mvn dependency:go-offline -pl services/api-gateway -am || true

# Copy source and build
COPY services/api-gateway/src ./services/api-gateway/src
COPY services/api-gateway/pom.xml ./services/api-gateway/
RUN mvn package -pl services/api-gateway -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/api-gateway/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
