# 서버 배포 및 민감정보 설정 가이드

## 📋 목차
1. [환경변수 설정 방법](#1-환경변수-설정-방법)
2. [서버 배포 방법](#2-서버-배포-방법)
3. [보안 모범 사례](#3-보안-모범-사례)
4. [문제 해결](#4-문제-해결)

---

## 1. 환경변수 설정 방법

### 방법 A: .env 파일 사용 (권장)

```bash
# 1. 서버에 접속
ssh steve@192.168.0.9

# 2. 프로젝트 디렉토리로 이동
cd ~/blitz

# 3. .env 파일 생성
cp .env.example .env

# 4. 실제 값으로 수정
nano .env  # 또는 vi .env
```

`.env` 파일 내용:
```bash
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=real
DB_HOST=localhost
DB_PASSWORD=실제_비밀번호
REDIS_PASSWORD=실제_Redis_비밀번호
GOOGLE_CLIENT_ID=실제_구글_클라이언트_ID
GOOGLE_CLIENT_SECRET=실제_구글_시크릿
# ... 나머지 설정
```

### 방법 B: Shell 환경변수로 직접 설정

```bash
# 1. 환경변수 설정 스크립트 수정
nano scripts/set-env.sh

# 2. 환경변수 로드
source scripts/set-env.sh

# 3. 확인
echo $DB_PASSWORD
```

### 방법 C: systemd 서비스 파일 사용

```bash
# 1. 서비스 파일 복사 및 수정
sudo cp scripts/blitz.service /etc/systemd/system/
sudo nano /etc/systemd/system/blitz.service

# 2. .env 파일 경로 또는 환경변수 직접 입력
# EnvironmentFile=/home/steve/blitz/.env

# 3. 서비스 재시작
sudo systemctl daemon-reload
sudo systemctl restart blitz
```

---

## 2. 서버 배포 방법

### 📦 방법 1: JAR 파일로 직접 실행

```bash
# 1. 프로젝트 클론
cd ~
git clone https://github.com/portuna85/blitz.git
cd blitz

# 2. 환경변수 설정
cp .env.example .env
nano .env  # 실제 값으로 수정

# 3. 빌드
./gradlew clean build -x test

# 4. 환경변수 로드 및 실행
export $(cat .env | xargs)
java -jar build/libs/blitz-*.jar

# 또는 백그라운드 실행
nohup java -jar build/libs/blitz-*.jar > logs/app.log 2>&1 &
```

### 🐳 방법 2: Docker Compose 사용 (권장)

```bash
# 1. 환경변수 파일 준비
cp .env.docker.example .env.docker
nano .env.docker  # 실제 값으로 수정

# 2. Docker Compose 실행
docker-compose --env-file .env.docker up -d

# 3. 로그 확인
docker-compose logs -f app

# 4. 중지
docker-compose down
```

### ⚙️ 방법 3: systemd 서비스로 등록

```bash
# 1. JAR 파일 빌드 및 배치
./gradlew clean build -x test
sudo mkdir -p /opt/blitz
sudo cp build/libs/blitz-*.jar /opt/blitz/blitz.jar

# 2. .env 파일 복사
sudo cp .env /opt/blitz/.env
sudo chown steve:steve /opt/blitz/.env
sudo chmod 600 /opt/blitz/.env

# 3. 서비스 파일 설정
sudo cp scripts/blitz.service /etc/systemd/system/
sudo nano /etc/systemd/system/blitz.service
# WorkingDirectory와 ExecStart 경로 확인

# 4. 서비스 시작
sudo systemctl daemon-reload
sudo systemctl enable blitz
sudo systemctl start blitz

# 5. 상태 확인
sudo systemctl status blitz
sudo journalctl -u blitz -f
```

---

## 3. 보안 모범 사례

### ✅ 필수 보안 체크리스트

1. **민감정보 파일 권한 설정**
```bash
chmod 600 .env
chmod 600 scripts/set-env.sh
chmod 700 secret/
```

2. **Git에서 민감정보 제외 확인**
```bash
# .gitignore에 다음 항목 확인
.env
.env.*
!.env.example
secret/
*secret*
```

3. **환경변수 암호화 (선택사항)**
```bash
# ansible-vault 사용 예시
ansible-vault encrypt .env
ansible-vault decrypt .env --output=-
```

4. **강력한 비밀번호 생성**
```bash
# 랜덤 비밀번호 생성
openssl rand -base64 32

# UUID 생성
uuidgen
```

### 🔒 비밀번호 관리 도구

- **AWS Secrets Manager** (클라우드)
- **HashiCorp Vault** (온프레미스)
- **1Password / LastPass** (팀 협업)

---

## 4. 문제 해결

### ❌ 환경변수가 로드되지 않을 때

```bash
# 1. 환경변수 확인
printenv | grep DB_
printenv | grep SPRING_

# 2. Spring Boot가 인식하는지 확인
java -jar blitz.jar --spring.config.location=file:./application.yml

# 3. 로그에서 에러 확인
tail -f logs/blitz.log
```

### ❌ 데이터베이스 연결 실패

```bash
# 1. DB 접속 테스트
mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p$DB_PASSWORD $DB_NAME

# 2. 방화벽 확인
sudo ufw status
sudo ufw allow 3306/tcp

# 3. Docker 네트워크 확인 (Docker 사용 시)
docker network ls
docker network inspect blitz_default
```

### ❌ Redis 연결 실패

```bash
# Redis 접속 테스트
redis-cli -h $REDIS_HOST -p $REDIS_PORT -a $REDIS_PASSWORD ping

# Redis 서버 상태
sudo systemctl status redis
```

---

## 5. 실전 배포 예시

### Ubuntu 서버에서 전체 배포 과정

```bash
# 1. 서버 접속
ssh steve@192.168.0.9

# 2. 필수 패키지 설치
sudo apt update
sudo apt install -y openjdk-17-jdk git mysql-client redis-tools

# 3. 프로젝트 클론
cd ~
git clone https://github.com/portuna85/blitz.git
cd blitz

# 4. 환경변수 설정
cp .env.example .env
nano .env
# 실제 비밀번호들을 입력

# 5. 빌드
./gradlew clean build -x test

# 6. 실행 테스트
export $(cat .env | xargs)
java -jar build/libs/blitz-*.jar

# 7. systemd 서비스 등록 (선택)
sudo cp scripts/blitz.service /etc/systemd/system/
sudo systemctl enable blitz
sudo systemctl start blitz
sudo systemctl status blitz
```

### 배포 자동화 스크립트

`scripts/deploy-to-server.sh`:
```bash
#!/bin/bash
set -e

SERVER="steve@192.168.0.9"
APP_DIR="/home/steve/blitz"

echo "🚀 서버 배포 시작..."

# 1. 로컬 빌드
./gradlew clean build -x test

# 2. JAR 파일 전송
scp build/libs/blitz-*.jar $SERVER:$APP_DIR/

# 3. 서버에서 재시작
ssh $SERVER << 'EOF'
cd ~/blitz
sudo systemctl restart blitz
sudo systemctl status blitz
EOF

echo "✅ 배포 완료!"
```

---

## 📚 추가 참고자료

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [12-Factor App - Config](https://12factor.net/config)
- [Docker Secrets](https://docs.docker.com/engine/swarm/secrets/)

---

**🔐 보안 주의사항**: 
- `.env` 파일은 절대 Git에 커밋하지 마세요!
- 프로덕션 비밀번호는 정기적으로 변경하세요!
- 최소 권한 원칙을 적용하세요!

