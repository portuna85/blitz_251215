# Blitz - Spring Boot Web Service

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-9.2.1-blue.svg)](https://gradle.org/)

Spring Boot 3.x 기반의 모던 웹 서비스 프로젝트입니다.

## 🚀 주요 기능

- ✅ 게시글 CRUD (Create, Read, Update, Delete)
- ✅ OAuth2 소셜 로그인 (Google, Naver)
- ✅ Redis 기반 세션 관리
- ✅ MariaDB 데이터 영속화
- ✅ RESTful API 설계
- ✅ Thymeleaf 템플릿 엔진
- ✅ Docker 기반 인프라 구성

## 🛠 기술 스택

### Backend
- **Java 21** - 최신 LTS 버전
- **Spring Boot 3.5.8** - 프레임워크
- **Spring Data JPA** - ORM
- **Spring Security** - 인증/인가
- **Spring Session** - 세션 관리
- **Hibernate** - JPA 구현체

### Frontend
- **Thymeleaf** - 서버 사이드 템플릿 엔진
- **Bootstrap 5.3.3** - UI 프레임워크
- **Vanilla JavaScript (ES6+)** - jQuery 제거, 모던 JavaScript 사용

### Database
- **MariaDB 10.11** - 메인 데이터베이스
- **Redis 7** - 세션 저장소
- **H2** - 테스트용 인메모리 DB

### DevOps
- **Docker & Docker Compose** - 컨테이너화
- **Gradle 9.2.1** - 빌드 도구
- **Git** - 버전 관리

## 📋 사전 요구사항

- Java 21 이상
- Docker Desktop (권장)
- Git

## 🏃 시작하기

### 1. 저장소 클론

```bash
git clone https://github.com/portuna85/blitz.git
cd blitz
```

### 2. 환경 변수 설정

`.env` 파일을 프로젝트 루트에 생성하고 다음 내용을 입력하세요:

```properties
# MariaDB (Docker)
DB_HOST=localhost
DB_PORT=3306
DB_NAME=kraft_db
DB_USER=kraft_user
DB_PASSWORD=your_password
DB_ROOT_PASSWORD=root
DB_TIMEZONE=Asia/Seoul

# Redis (Docker)
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your_redis_password

# MinIO (Docker)
MINIO_HOST=localhost
MINIO_PORT=9000
MINIO_CONSOLE_PORT=9001
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=your_minio_password
MINIO_BUCKET=kraft-bucket

# OAuth2 Google
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

# OAuth2 Naver
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret

# Server
SERVER_PORT=8080
```

### 3. Docker 인프라 실행

```bash
# Docker Compose로 MariaDB, Redis, MinIO 실행
docker-compose up -d

# 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f
```

### 4. 애플리케이션 실행

#### Gradle로 실행
```bash
# Windows
gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

#### JAR 파일로 실행
```bash
# 빌드
./gradlew clean build

# 실행
java -jar build/libs/blitz-1.0.4-SNAPSHOT.jar
```

#### IntelliJ IDEA에서 실행
1. `.env` 파일의 환경 변수를 시스템 환경 변수로 설정
2. Run Configuration 생성
3. Main class: `com.blitz.springboot.Application`
4. Active profiles: `local`
5. Environment variables에 `.env` 내용 추가

### 5. 접속

- 웹 애플리케이션: http://localhost:8080
- API 엔드포인트: http://localhost:8080/api/v1/posts

## 🧪 테스트

```bash
# 전체 테스트 실행
./gradlew test

# 특정 테스트 클래스 실행
./gradlew test --tests ProfileControllerTest

# 테스트 리포트 확인
# build/reports/tests/test/index.html
```

## 📁 프로젝트 구조

```
blitz/
├── src/
│   ├── main/
│   │   ├── java/com/blitz/springboot/
│   │   │   ├── config/           # 설정 클래스
│   │   │   ├── domain/           # 엔티티 및 레포지토리
│   │   │   ├── service/          # 비즈니스 로직
│   │   │   ├── web/              # 컨트롤러 및 DTO
│   │   │   └── Application.java  # 메인 클래스
│   │   └── resources/
│   │       ├── application.yml   # 메인 설정
│   │       ├── application-*.yml # 프로파일별 설정
│   │       ├── logback-spring.xml # 로깅 설정
│   │       ├── static/           # 정적 리소스
│   │       └── templates/        # Thymeleaf 템플릿
│   └── test/                     # 테스트 코드
├── docker-compose.yml            # Docker 구성
├── .env                          # 환경 변수 (gitignore)
├── .gitignore                    # Git 무시 파일
├── build.gradle                  # Gradle 빌드 설정
└── README.md                     # 프로젝트 문서
```

## 🌍 프로파일

- `local` - 로컬 개발 환경 (기본값)
- `real` - 운영 환경
- `real1` - 운영 환경 1 (Blue)
- `real2` - 운영 환경 2 (Green)
- `test` - 테스트 환경 (H2 DB)

## 📡 API 엔드포인트

### Posts API

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/v1/posts` | 게시글 목록 조회 | No |
| GET | `/api/v1/posts/{id}` | 게시글 상세 조회 | No |
| POST | `/api/v1/posts` | 게시글 등록 | Yes |
| PUT | `/api/v1/posts/{id}` | 게시글 수정 | Yes |
| DELETE | `/api/v1/posts/{id}` | 게시글 삭제 | Yes |

### 인증 API

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/oauth2/authorization/google` | Google 로그인 |
| GET | `/oauth2/authorization/naver` | Naver 로그인 |
| GET | `/logout` | 로그아웃 |

## 🏗 아키텍처

### SOLID 원칙 준수

이 프로젝트는 객체지향 설계의 SOLID 원칙을 철저히 준수합니다:

#### 1. **SRP (Single Responsibility Principle)** - 단일 책임 원칙
- **Controller**: HTTP 요청/응답 처리만 담당
- **Service**: 비즈니스 로직 처리만 담당
- **Repository**: 데이터 접근만 담당
- **BaseEntity**: Auditing 기능만 담당

#### 2. **OCP (Open-Closed Principle)** - 개방-폐쇄 원칙
- **OAuth2 Provider Strategy Pattern**: 새로운 OAuth 제공자 추가 시 기존 코드 수정 없이 확장 가능
  ```java
  // OAuth2Provider 인터페이스 구현만으로 확장
  @Component
  public class KakaoOAuth2Provider implements OAuth2Provider {
      // 구현
  }
  ```
- **Service 인터페이스**: 구현체 교체 가능

#### 3. **LSP (Liskov Substitution Principle)** - 리스코프 치환 원칙
- OAuth2Provider 구현체들은 동일한 인터페이스를 제공하여 교체 가능
- Role enum의 각 권한은 동일한 인터페이스 제공

#### 4. **ISP (Interface Segregation Principle)** - 인터페이스 분리 원칙
- OAuth2Provider: Provider별로 필요한 메서드만 정의
- Service 인터페이스: 클라이언트가 필요한 메서드만 노출

#### 5. **DIP (Dependency Inversion Principle)** - 의존 역전 원칙
- Controller → Service 인터페이스 의존
- Service → Repository 인터페이스 의존
- CustomOAuth2UserService → OAuth2ProviderFactory 인터페이스 의존
- 생성자 주입(@RequiredArgsConstructor)을 통한 느슨한 결합

### OAuth2 Provider 아키텍처

```
OAuth2 로그인 요청
    ↓
CustomOAuth2UserService
    ↓
OAuth2ProviderFactory
    ↓
┌─────────────────────────┐
│ OAuth2Provider (인터페이스) │
├─────────────────────────┤
│ GoogleOAuth2Provider    │
│ NaverOAuth2Provider     │
│ (확장 가능)               │
└─────────────────────────┘
    ↓
User 엔티티 생성/업데이트
```

### 도메인 엔티티 설계

- **불변성 강화**: `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
- **검증 로직 내장**: 생성자/업데이트 메서드에서 유효성 검증
- **책임 분리**: BaseEntity에서 공통 Auditing 필드 관리

### 레이어 구조

```
┌─────────────────────┐
│   Presentation      │  Controller (HTTP 요청/응답)
├─────────────────────┤
│   Application       │  Service (비즈니스 로직)
├─────────────────────┤
│   Domain            │  Entity, Repository (도메인 로직)
├─────────────────────┤
│   Common            │  BaseEntity, Exception, Util
├─────────────────────┤
│   Infrastructure    │  JPA, Redis, OAuth2, External APIs
└─────────────────────┘
```

### 패키지 구조 원칙

```
com.blitz.springboot/
├── common/              # 공통 컴포넌트
│   ├── BaseEntity      # 모든 엔티티의 기본 클래스
│   └── exception/      # 예외 처리
├── config/              # 설정 클래스
│   └── auth/           # 인증/인가 설정
│       └── dto/        # OAuth2 Provider 전략
├── domain/              # 도메인 레이어
│   ├── posts/          # 게시글 도메인
│   └── user/           # 사용자 도메인
├── service/             # 비즈니스 로직
└── web/                 # 프레젠테이션 레이어
    ├── dto/            # 데이터 전송 객체
    └── *Controller     # 컨트롤러
```

## 📊 로깅

로그는 레벨별로 분리되어 저장됩니다:

- `logs/debug/*.log` - DEBUG 레벨
- `logs/info/*.log` - INFO 레벨
- `logs/warn/*.log` - WARN 레벨
- `logs/error/*.log` - ERROR 레벨

테스트 실행 시 로그 파일은 생성되지 않습니다.

## 🔒 보안

- Spring Security를 통한 인증/인가
- OAuth2 소셜 로그인
- CSRF 보호
- Session Fixation 방어
- 환경 변수를 통한 민감 정보 관리

## 🐳 Docker 명령어

```bash
# 컨테이너 시작
docker-compose up -d

# 컨테이너 중지
docker-compose down

# 로그 확인
docker-compose logs -f [service-name]

# 컨테이너 재시작
docker-compose restart

# 볼륨 포함 완전 삭제
docker-compose down -v
```

## 🤝 기여

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 👨‍💻 개발자

- GitHub: [@portuna85](https://github.com/portuna85)

## 📚 참고 자료

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Docker Documentation](https://docs.docker.com/)

---

⭐ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!


### 개발 환경 (default, local)
- DEBUG 로그 활성화
- H2 인메모리 DB 사용 가능
- 모든 레벨의 로그 파일 생성
- 콘솔 + 파일 출력

### 운영 환경 (real, real1, real2)
- INFO 레벨 로깅
- MariaDB 사용
- INFO/WARN/ERROR 로그만 파일 저장
- real1: 8081 포트
- real2: 8082 포트

### 테스트 환경 (test)
- DEBUG 로그 활성화
- H2 인메모리 DB 사용
- **콘솔만 출력 (파일 생성 안 함)**

## 로그 파일

로그는 `logs/` 디렉토리에 레벨별로 저장됩니다:

```
logs/
├── blitz.log              # 전체 (30일)
├── debug/blitz-debug.log  # DEBUG (7일)
├── info/blitz-info.log    # INFO (30일)
├── warn/blitz-warn.log    # WARN (60일)
└── error/blitz-error.log  # ERROR (90일)
```

## OAuth2 설정

### Google
1. [Google Cloud Console](https://console.cloud.google.com/)에서 프로젝트 생성
2. OAuth 2.0 클라이언트 ID 생성
3. 승인된 리디렉션 URI: `http://localhost:8080/login/oauth2/code/google`

### Naver
1. [Naver Developers](https://developers.naver.com/)에서 애플리케이션 등록
2. Callback URL: `http://localhost:8080/login/oauth2/code/naver`

## 주요 기능

- 게시글 CRUD
- 소셜 로그인 (Google, Naver)
- Redis 기반 세션 관리
- 레벨별 로그 파일 분리
- 무중단 배포 지원 (real1, real2)

## 보안

⚠️ **절대 커밋하지 마세요:**
- `.env` 파일
- `logs/` 디렉토리
- `secret/` 폴더의 실제 정보

`.gitignore`에 이미 등록되어 있습니다. 



