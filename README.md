# Blitz - Spring Boot Web Service

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-9.2.1-blue.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

> Spring Boot 3.x와 현대적인 아키텍처 패턴을 적용한 엔터프라이즈급 웹 서비스 프로젝트

## 📖 목차

- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [아키텍처](#-아키텍처)
- [시작하기](#-시작하기)
- [환경 설정](#-환경-설정)
- [API 문서](#-api-문서)
- [CI/CD](#-cicd-파이프라인)
- [모니터링](#-모니터링)
- [보안](#-보안)

---

## 🚀 주요 기능

### 핵심 기능
- ✅ **게시글 관리**: CRUD 작업 및 작성자 권한 검증
- ✅ **소셜 로그인**: OAuth2 기반 Google, Naver 인증
- ✅ **세션 관리**: Redis 기반 분산 세션 처리
- ✅ **권한 제어**: Spring Security를 통한 세밀한 접근 제어

### 인프라 & DevOps
- ✅ **컨테이너화**: Docker 기반 마이크로서비스 아키텍처
- ✅ **CI/CD**: GitHub Actions 자동화 파이프라인
- ✅ **보안 스캔**: Trivy를 통한 취약점 자동 검사
- ✅ **모니터링**: Actuator & Prometheus 메트릭 수집
- ✅ **무중단 배포**: Blue-Green 배포 전략 지원

---

## 🛠 기술 스택

### Backend Core
| 기술 | 버전 | 용도 |
|------|------|------|
| Java | 21 (LTS) | 런타임 환경 |
| Spring Boot | 3.5.8 | 애플리케이션 프레임워크 |
| Spring Data JPA | 3.5.8 | ORM & 데이터 접근 |
| Spring Security | 6.x | 인증/인가 |
| Spring Session | 3.x | 분산 세션 관리 |
| Hibernate | 6.x | JPA 구현체 |

### Frontend
| 기술 | 버전 | 용도 |
|------|------|------|
| Thymeleaf | 3.x | 서버사이드 템플릿 엔진 |
| Bootstrap | 5.3.3 | UI 프레임워크 |
| ES6+ JavaScript | - | 모던 클라이언트 로직 |

### Database & Cache
| 기술 | 버전 | 용도 |
|------|------|------|
| MariaDB | 10.11 | 메인 RDBMS |
| Redis | 7 | 세션 저장소 & 캐시 |
| H2 | 2.x | 테스트용 In-Memory DB |

### DevOps & Monitoring
| 기술 | 버전 | 용도 |
|------|------|------|
| Docker | Latest | 컨테이너화 |
| Docker Compose | 3.8 | 로컬 오케스트레이션 |
| GitHub Actions | - | CI/CD 자동화 |
| Trivy | Latest | 컨테이너 보안 스캔 |
| Actuator | 3.5.8 | 헬스체크 & 메트릭 |
| Prometheus | - | 메트릭 수집 |
| Gradle | 9.2.1 | 빌드 도구 |

---

## 🏗 아키텍처

### SOLID 원칙 기반 설계

본 프로젝트는 객체지향 설계의 5대 원칙(SOLID)을 엄격히 준수합니다.

#### 1️⃣ SRP (Single Responsibility Principle)
각 계층은 단일 책임만 가집니다:
- **Controller**: HTTP 요청/응답 처리
- **Service**: 비즈니스 로직 수행
- **Repository**: 데이터 접근 관리
- **Entity**: 도메인 모델 표현

#### 2️⃣ OCP (Open-Closed Principle)
확장에는 열려있고 수정에는 닫혀있습니다:
```java
// 새로운 OAuth Provider 추가 시 기존 코드 수정 불필요
@Component
public class KakaoOAuth2Provider implements OAuth2Provider {
    @Override
    public OAuthAttributes convertAttributes(Map<String, Object> attributes) {
        // Kakao 전용 구현
    }
}
```

#### 3️⃣ LSP (Liskov Substitution Principle)
하위 타입은 상위 타입을 완전히 대체 가능합니다:
- 모든 OAuth2Provider 구현체는 동일한 계약을 준수
- Role enum의 모든 값은 동일한 인터페이스 제공

#### 4️⃣ ISP (Interface Segregation Principle)
클라이언트는 필요한 메서드만 의존합니다:
- 각 Service 인터페이스는 특정 도메인 작업만 정의
- OAuth2Provider는 필수 메서드만 선언

#### 5️⃣ DIP (Dependency Inversion Principle)
고수준 모듈은 저수준 모듈에 의존하지 않습니다:
- Controller → Service 인터페이스 의존
- Service → Repository 인터페이스 의존
- 생성자 주입으로 느슨한 결합 유지

### 계층형 아키텍처

```
┌──────────────────────────────────────────┐
│         Presentation Layer               │
│    (Controller, View, DTO)               │
├──────────────────────────────────────────┤
│         Application Layer                │
│         (Service, Facade)                │
├──────────────────────────────────────────┤
│           Domain Layer                   │
│    (Entity, Repository, Domain Logic)    │
├──────────────────────────────────────────┤
│          Infrastructure Layer            │
│   (JPA, Redis, OAuth2, External API)     │
└──────────────────────────────────────────┘
```

### OAuth2 인증 흐름

```
     클라이언트 요청
            ↓
    Spring Security Filter
            ↓
   CustomOAuth2UserService
            ↓
   OAuth2ProviderFactory
            ↓
   ┌─────────────────────┐
   │  OAuth2Provider     │
   ├─────────────────────┤
   │ GoogleProvider      │
   │ NaverProvider       │
   │ (확장 가능)          │
   └─────────────────────┘
            ↓
      User Entity
            ↓
    세션 생성 (Redis)
```

### 패키지 구조

```
com.blitz.springboot/
├── common/                    # 공통 컴포넌트
│   ├── BaseEntity.java       # JPA Auditing 베이스
│   └── exception/            # 커스텀 예외
│       ├── ErrorCode.java
│       ├── GlobalExceptionHandler.java
│       └── UnauthorizedException.java
├── config/                    # 설정 클래스
│   ├── security/
│   │   ├── SecurityConfig.java
│   │   └── oauth/
│   │       ├── CustomOAuth2UserService.java
│   │       ├── OAuth2Provider.java
│   │       └── OAuth2ProviderFactory.java
│   └── WebConfig.java
├── domain/                    # 도메인 계층
│   ├── posts/
│   │   ├── Posts.java        # 엔티티
│   │   ├── PostsRepository.java
│   │   ├── dto/
│   │   └── service/
│   │       ├── PostsService.java
│   │       └── PostsServiceImpl.java
│   └── user/
│       ├── User.java
│       ├── UserRepository.java
│       └── Role.java
└── web/                       # 프레젠테이션 계층
    ├── api/
    │   └── PostsApiController.java
    └── IndexController.java
```

---

## 🚀 시작하기

### 사전 요구사항

- **Java 21** 이상
- **Docker Desktop** (권장) 또는 로컬 MariaDB/Redis 설치
- **Git**

### 1. 프로젝트 클론

```bash
git clone https://github.com/portuna85/blitz.git
cd blitz
```

### 2. 환경 변수 설정

프로젝트 루트에 `.env` 파일을 생성하세요:

```properties
# Database Configuration
DB_HOST=localhost
DB_PORT=3306
DB_NAME=kraft_db
DB_USER=kraft_user
DB_PASSWORD=your_secure_password
DB_ROOT_PASSWORD=your_root_password
DB_TIMEZONE=Asia/Seoul

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your_redis_password

# MinIO Configuration (Optional - for file storage)
MINIO_HOST=localhost
MINIO_PORT=9000
MINIO_CONSOLE_PORT=9001
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=your_minio_password
MINIO_BUCKET=kraft-bucket

# OAuth2 - Google
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# OAuth2 - Naver
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret

# Application
SERVER_PORT=8080
```

⚠️ **보안 주의**: `.env` 파일은 절대 Git에 커밋하지 마세요!

### 3. 인프라 실행 (Docker)

```bash
# 전체 인프라 시작
docker-compose up -d

# 상태 확인
docker-compose ps

# 로그 모니터링
docker-compose logs -f
```

### 4. 애플리케이션 실행

**방법 1: Gradle 직접 실행**
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

**방법 2: JAR 빌드 후 실행**
```bash
./gradlew clean build
java -jar build/libs/blitz-1.0.4-SNAPSHOT.jar
```

**방법 3: IntelliJ IDEA**
1. `Application.java` 열기
2. Run Configuration 생성
3. Active profiles: `local`
4. Environment variables: `.env` 내용 추가
5. 실행 (Shift+F10)

### 5. 접속 확인

| 서비스 | URL |
|--------|-----|
| 웹 애플리케이션 | http://localhost:8080 |
| API 엔드포인트 | http://localhost:8080/api/v1/posts |
| Actuator 헬스체크 | http://localhost:8080/actuator/health |
| H2 콘솔 (local) | http://localhost:8080/h2-console |

---

## ⚙️ 환경 설정

### 프로파일 전략

| 프로파일 | 용도 | DB | 포트 | 로그 레벨 |
|---------|------|-------|------|-----------|
| `local` | 로컬 개발 | H2/MariaDB | 8080 | DEBUG |
| `test` | 자동화 테스트 | H2 (In-Memory) | - | DEBUG |
| `real` | 운영 환경 | MariaDB | 8080 | INFO |
| `real1` | Blue 환경 | MariaDB | 8081 | INFO |
| `real2` | Green 환경 | MariaDB | 8082 | INFO |

### 로깅 설정

로그는 `logs/` 디렉토리에 레벨별로 분리 저장됩니다:

```
logs/
├── blitz.log              # 전체 로그 (보존: 30일)
├── debug/
│   └── blitz-debug.log    # DEBUG 레벨 (보존: 7일)
├── info/
│   └── blitz-info.log     # INFO 레벨 (보존: 30일)
├── warn/
│   └── blitz-warn.log     # WARN 레벨 (보존: 60일)
└── error/
    └── blitz-error.log    # ERROR 레벨 (보존: 90일)
```

**프로파일별 로그 전략:**
- `local/real`: 콘솔 + 파일 출력
- `test`: 콘솔 출력만 (파일 생성 안 함)

### OAuth2 설정 가이드

#### Google OAuth2
1. [Google Cloud Console](https://console.cloud.google.com/) 접속
2. 프로젝트 생성 → API 및 서비스 → OAuth 동의 화면 설정
3. 사용자 인증 정보 → OAuth 2.0 클라이언트 ID 생성
4. 승인된 리디렉션 URI 추가:
   ```
   http://localhost:8080/login/oauth2/code/google
   ```
5. 발급된 `Client ID`와 `Client Secret`을 `.env`에 설정

#### Naver OAuth2
1. [Naver Developers](https://developers.naver.com/) 접속
2. 애플리케이션 등록 → 사용 API: 네이버 로그인
3. Callback URL 설정:
   ```
   http://localhost:8080/login/oauth2/code/naver
   ```
4. 발급된 `Client ID`와 `Client Secret`을 `.env`에 설정

---

## 📡 API 문서

### Posts API

#### 게시글 목록 조회
```http
GET /api/v1/posts
```
**응답:**
```json
[
  {
    "id": 1,
    "title": "제목",
    "author": "작성자",
    "modifiedDate": "2025-12-03T10:00:00"
  }
]
```

#### 게시글 상세 조회
```http
GET /api/v1/posts/{id}
```

#### 게시글 등록 (인증 필요)
```http
POST /api/v1/posts
Content-Type: application/json

{
  "title": "제목",
  "content": "내용",
  "author": "작성자"
}
```

#### 게시글 수정 (작성자만)
```http
PUT /api/v1/posts/{id}
Content-Type: application/json

{
  "title": "수정된 제목",
  "content": "수정된 내용"
}
```

#### 게시글 삭제 (작성자만)
```http
DELETE /api/v1/posts/{id}
```

### 인증 API

| 엔드포인트 | 설명 |
|-----------|------|
| `GET /oauth2/authorization/google` | Google 로그인 |
| `GET /oauth2/authorization/naver` | Naver 로그인 |
| `GET /logout` | 로그아웃 |

### Actuator API

| 엔드포인트 | 설명 | 인증 필요 |
|-----------|------|-----------|
| `GET /actuator/health` | 전체 헬스체크 | ❌ |
| `GET /actuator/health/liveness` | Liveness Probe | ❌ |
| `GET /actuator/health/readiness` | Readiness Probe | ❌ |
| `GET /actuator/info` | 애플리케이션 정보 | ❌ |
| `GET /actuator/metrics` | 메트릭 목록 | ✅ |
| `GET /actuator/prometheus` | Prometheus 포맷 메트릭 | ✅ |

---

## 🔄 CI/CD 파이프라인

### CI (Continuous Integration)

**트리거:** `main`, `develop` 브랜치 Push 또는 Pull Request

**워크플로우:**
```yaml
1. ✅ 코드 체크아웃
2. ✅ JDK 21 설정 + Gradle 캐싱
3. ✅ 빌드 & 테스트 실행
4. ✅ 테스트 결과 업로드
5. ✅ 빌드 아티팩트 저장 (JAR)
6. ✅ PR에 테스트 리포트 코멘트
```

### CD (Continuous Deployment)

**트리거:** `main` 브랜치 Push

**워크플로우:**
```yaml
1. ✅ Docker 멀티스테이지 빌드
2. ✅ 레이어 캐싱으로 빌드 최적화
3. ✅ GHCR(GitHub Container Registry)에 푸시
4. ✅ Trivy 보안 스캔 실행
5. ✅ SSH로 운영 서버 배포
6. ✅ 헬스체크 수행
7. ✅ 실패 시 자동 롤백
```

### Qodana 코드 품질

**트리거:** `main` Push, PR, 수동 실행

**검사 항목:**
- 코드 품질 분석
- 보안 취약점 탐지
- 코드 스멜 감지
- PR 자동 코멘트

---

## 📊 모니터링

### Actuator Endpoints

Spring Boot Actuator를 통해 애플리케이션 상태를 실시간으로 모니터링할 수 있습니다.

**헬스체크:**
```bash
curl http://localhost:8080/actuator/health
```

**응답 예시:**
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "redis": { "status": "UP" },
    "diskSpace": { "status": "UP" }
  }
}
```

### Prometheus 메트릭

```bash
curl http://localhost:8080/actuator/prometheus
```

**주요 메트릭:**
- `jvm_memory_used_bytes`: JVM 메모리 사용량
- `http_server_requests_seconds`: HTTP 요청 통계
- `hikaricp_connections`: DB 커넥션 풀 상태

---

## 🔒 보안

### 적용된 보안 메커니즘

| 보안 기능 | 설명 |
|----------|------|
| **Spring Security** | 인증/인가 프레임워크 |
| **OAuth2** | 소셜 로그인 (Google, Naver) |
| **CSRF 보호** | Cross-Site Request Forgery 방어 |
| **Session Fixation 방어** | 로그인 시 세션 재생성 |
| **환경 변수 관리** | 민감 정보 분리 |
| **Trivy 스캔** | 컨테이너 취약점 검사 |

### 보안 체크리스트

⚠️ **절대 커밋 금지:**
- `.env` 파일 (환경 변수)
- `logs/` 디렉토리 (로그 파일)
- `secret/` 폴더 (시크릿 정보)

✅ **권장 사항:**
- 운영 환경에서는 강력한 비밀번호 사용
- 정기적인 의존성 업데이트
- HTTPS 사용 (운영 환경)
- API Rate Limiting 적용 (필요시)

---

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트만 실행
./gradlew test --tests PostsServiceTest

# 테스트 커버리지 리포트
./gradlew test jacocoTestReport

# 리포트 확인
open build/reports/tests/test/index.html
```

---

## 🐳 Docker 명령어

```bash
# 인프라 시작
docker-compose up -d

# 특정 서비스만 시작
docker-compose up -d mariadb redis

# 로그 확인
docker-compose logs -f [service-name]

# 컨테이너 재시작
docker-compose restart

# 전체 중지
docker-compose down

# 볼륨까지 삭제
docker-compose down -v
```

---

## 🤝 기여 가이드

1. 이 저장소를 Fork 합니다
2. Feature 브랜치 생성 (`git checkout -b feature/AmazingFeature`)
3. 변경사항 커밋 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 Push (`git push origin feature/AmazingFeature`)
5. Pull Request 생성

---

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

---

## 👨‍💻 개발자

**Portuna**
- GitHub: [@portuna85](https://github.com/portuna85)
- Email: portuna85@gmail.com (업데이트 필요시 수정)

---

## 📚 참고 자료

### 공식 문서
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Docker Documentation](https://docs.docker.com/)

### 학습 자료
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)
- [SOLID Principles](https://www.baeldung.com/solid-principles)
- [OAuth 2.0 RFC](https://oauth.net/2/)

---

<div align="center">

### ⭐ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!

**Made with ❤️ by Portuna**

</div> 



