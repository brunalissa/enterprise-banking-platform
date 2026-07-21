# Multi-stage Dockerfile for customer-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/customer-service/pom.xml ./services/customer-service/
RUN mvn dependency:go-offline -pl services/customer-service -am || true

# Copy source and build
COPY services/customer-service/src ./services/customer-service/src
COPY services/customer-service/pom.xml ./services/customer-service/
RUN mvn package -pl services/customer-service -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/customer-service/target/*.jar app.jar

EXPOSE 8082

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
