# 1. 빌드 단계
FROM gradle:8.9-jdk21 AS builder
WORKDIR /workspace
COPY . .
RUN gradle clean bootJar --no-daemon

# 2. 런타임 단계
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
