# 1. 빌드 단계 - Gradle 캐싱 최적화
FROM gradle:8.9-jdk21 AS builder
WORKDIR /workspace

# Gradle 의존성 캐싱을 위해 빌드 파일만 먼저 복사
COPY build.gradle settings.gradle ./
COPY gradle gradle

# 의존성 다운로드 (캐시 레이어)
RUN gradle dependencies --no-daemon || true

# 소스 코드 복사 및 빌드
COPY . .
RUN gradle clean bootJar --no-daemon

# 2. 런타임 단계 - 경량화 및 보안 강화
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 보안: non-root 사용자 생성
RUN addgroup -S spring && adduser -S spring -G spring

# JAR 파일 복사
COPY --from=builder /workspace/build/libs/*.jar app.jar

# 소유권 변경
RUN chown spring:spring app.jar

# non-root 사용자로 전환
USER spring

EXPOSE 8080

# 헬스체크 추가 (Spring Boot Actuator 사용 시)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM 메모리 최적화 옵션 추가
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
