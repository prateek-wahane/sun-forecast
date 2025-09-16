# ===== Build stage =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests clean package

# ===== Run stage =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/sun-forecast-0.0.1-SNAPSHOT.jar app.jar

# JVM optimizations for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Optional: pass your OpenAI key as env var
ENV OPENAI_API_KEY=""

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
