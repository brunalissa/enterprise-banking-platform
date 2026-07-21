# Multi-stage Dockerfile for payment-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/payment-service/pom.xml ./services/payment-service/
RUN mvn dependency:go-offline -pl services/payment-service -am || true

# Copy source and build
COPY services/payment-service/src ./services/payment-service/src
COPY services/payment-service/pom.xml ./services/payment-service/
RUN mvn package -pl services/payment-service -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/payment-service/target/*.jar app.jar

EXPOSE 8085

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
