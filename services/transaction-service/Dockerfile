# Multi-stage Dockerfile for transaction-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/transaction-service/pom.xml ./services/transaction-service/
RUN mvn dependency:go-offline -pl services/transaction-service -am || true

# Copy source and build
COPY services/transaction-service/src ./services/transaction-service/src
COPY services/transaction-service/pom.xml ./services/transaction-service/
RUN mvn package -pl services/transaction-service -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/transaction-service/target/*.jar app.jar

EXPOSE 8084

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
