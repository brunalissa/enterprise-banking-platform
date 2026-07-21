# Multi-stage Dockerfile for account-service
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy parent POM and install dependencies
COPY pom.xml ./
COPY services/account-service/pom.xml ./services/account-service/
RUN mvn dependency:go-offline -pl services/account-service -am || true

# Copy source and build
COPY services/account-service/src ./services/account-service/src
COPY services/account-service/pom.xml ./services/account-service/
RUN mvn package -pl services/account-service -am -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/account-service/target/*.jar app.jar

EXPOSE 8083

ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=docker

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
