# Blitz - Spring Boot Web Service

Spring Boot 기반의 웹 서비스 프로젝트입니다.

## 기술 스택

* **Java 21**
* **Spring Boot 3.5.8**
* **Gradle 8.x**
* **MariaDB** - 메인 데이터베이스
* **Redis** - 세션 저장소
* **OAuth2** - Google, Naver 소셜 로그인
* **JPA/Hibernate** - ORM
* **Mustache** - 템플릿 엔진

## 시작하기

### 1. 환경 변수 설정

`.env.example` 파일을 복사하여 `.env` 파일을 생성하고 실제 값을 입력하세요.

```bash
# Windows
copy .env.example .env

# Linux/Mac
cp .env.example .env
```

**필수 환경 변수:**
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` - MariaDB 연결 정보
- `REDIS_HOST`, `REDIS_PORT` - Redis 연결 정보
- `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET` - Google OAuth2
- `NAVER_CLIENT_ID`, `NAVER_CLIENT_SECRET` - Naver OAuth2

### 2. Docker 환경 설정 (권장)

#### Docker Desktop 설치

Windows:
```powershell
# winget 사용
winget install Docker.DockerDesktop

# 또는 https://www.docker.com/products/docker-desktop 에서 다운로드
```

#### 인프라 실행

```powershell
# Windows PowerShell
.\docker-start.ps1

# 또는 직접 실행
docker-compose up -d
```

컨테이너가 시작되면:
- **MariaDB**: `localhost:3306`
- **Redis**: `localhost:6379`

#### 컨테이너 관리

```powershell
# 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f

# 중지
.\docker-stop.ps1
# 또는
docker-compose down

# 데이터 초기화 (볼륨 삭제)
docker-compose down -v
```

### 3. 애플리케이션 실행

```bash
# 개발 환경
./gradlew bootRun

# 빌드 후 실행
./gradlew build
java -jar build/libs/blitz-1.0.4-SNAPSHOT.jar

# 특정 프로파일로 실행
./gradlew bootRun --args='--spring.profiles.active=real'
```

## 테스트

```bash
./gradlew test
```

## 프로파일

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



