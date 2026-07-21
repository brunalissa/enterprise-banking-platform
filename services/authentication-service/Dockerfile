# Multi-stage Dockerfile for authentication-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/authentication-service/pom.xml ./services/authentication-service/
RUN mvn dependency:go-offline -pl services/authentication-service -am || true

# Copy source and build
COPY services/authentication-service/src ./services/authentication-service/src
COPY services/authentication-service/pom.xml ./services/authentication-service/
RUN mvn package -pl services/authentication-service -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/authentication-service/target/*.jar app.jar

EXPOSE 8081

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
