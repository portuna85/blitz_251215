# 서버 민감정보 설정 빠른 시작 가이드

## 📋 요약

이 가이드는 Ubuntu 서버(192.168.0.9)에서 Blitz 애플리케이션을 배포할 때 민감정보를 안전하게 설정하는 방법을 설명합니다.

---

## 🎯 3가지 주요 방법

### 방법 1: .env 파일 사용 (가장 간단)

```bash
# 1. 서버 접속
ssh steve@192.168.0.9

# 2. 프로젝트 클론
cd ~
git clone https://github.com/portuna85/blitz.git
cd blitz

# 3. .env 파일 생성
cp .env.example .env
nano .env
```

**편집할 내용:**
```bash
# 실제 값으로 변경하세요
DB_PASSWORD=진짜_비밀번호_여기에
REDIS_PASSWORD=Redis_비밀번호_여기에
GOOGLE_CLIENT_ID=구글_클라이언트_ID
GOOGLE_CLIENT_SECRET=구글_시크릿
NAVER_CLIENT_ID=네이버_클라이언트_ID
NAVER_CLIENT_SECRET=네이버_시크릿
```

```bash
# 4. 파일 보호
chmod 600 .env

# 5. 환경변수 확인
./scripts/check-env.sh

# 6. 실행
export $(cat .env | xargs)
./gradlew bootRun
```

---

### 방법 2: Shell 스크립트 사용

```bash
# 1. 환경변수 스크립트 수정
cd ~/blitz
nano scripts/set-env.sh

# 2. 실제 값으로 변경
export DB_PASSWORD="실제_비밀번호"
export REDIS_PASSWORD="실제_Redis_비밀번호"
# ... 나머지 수정

# 3. 실행 가능하게 만들기
chmod +x scripts/set-env.sh
chmod 600 scripts/set-env.sh

# 4. 환경변수 로드
source scripts/set-env.sh

# 5. 확인
echo $DB_PASSWORD

# 6. 애플리케이션 실행
./gradlew bootRun
```

---

### 방법 3: systemd 서비스 (운영 환경 권장)

```bash
# 1. 빌드
cd ~/blitz
./gradlew clean build -x test

# 2. JAR 파일 배치
sudo mkdir -p /opt/blitz
sudo cp build/libs/blitz-*.jar /opt/blitz/blitz.jar

# 3. .env 파일 복사 (위에서 만든 것)
sudo cp .env /opt/blitz/.env
sudo chown steve:steve /opt/blitz/.env
sudo chmod 600 /opt/blitz/.env

# 4. 서비스 파일 복사 및 수정
sudo cp scripts/blitz.service /etc/systemd/system/
sudo nano /etc/systemd/system/blitz.service

# WorkingDirectory와 경로 확인:
# WorkingDirectory=/opt/blitz
# ExecStart=/usr/bin/java -jar /opt/blitz/blitz.jar
# EnvironmentFile=/opt/blitz/.env

# 5. 서비스 등록 및 시작
sudo systemctl daemon-reload
sudo systemctl enable blitz
sudo systemctl start blitz

# 6. 상태 확인
sudo systemctl status blitz

# 7. 로그 확인
sudo journalctl -u blitz -f
```

---

## 🔐 보안 체크리스트

### ✅ 반드시 할 것

```bash
# 1. .env 파일 권한 설정
chmod 600 .env

# 2. Git에 민감정보 추가되지 않았는지 확인
cat .gitignore | grep .env
# 출력: .env (있어야 함)

# 3. 강력한 비밀번호 생성
openssl rand -base64 32

# 4. 파일이 Git에 추적되지 않는지 확인
git status
# .env가 "Untracked files"에 나타나지 않아야 함
```

### ❌ 절대 하지 말 것

```bash
# 이것들은 절대 하지 마세요!
git add .env                    # ❌ .env를 Git에 추가
git commit -m "add passwords"   # ❌ 비밀번호 커밋
git push origin main            # ❌ 비밀번호 푸시
chmod 777 .env                  # ❌ 모든 사람이 읽을 수 있게 설정
```

---

## 🚀 실전 예시: 처음부터 끝까지

### 시나리오: Ubuntu 서버에 처음 배포

```bash
# 1단계: 서버 접속
ssh steve@192.168.0.9

# 2단계: 필수 패키지 설치
sudo apt update
sudo apt install -y openjdk-17-jdk git

# 3단계: 프로젝트 클론
cd ~
git clone https://github.com/portuna85/blitz.git
cd blitz

# 4단계: 환경변수 설정
cp .env.example .env
nano .env

# 다음 내용을 실제 값으로 수정:
# DB_PASSWORD=YourStrongPassword123!
# REDIS_PASSWORD=RedisPassword456!
# GOOGLE_CLIENT_ID=123456789.apps.googleusercontent.com
# GOOGLE_CLIENT_SECRET=GOCSPX-abcdefg
# NAVER_CLIENT_ID=naverClientId
# NAVER_CLIENT_SECRET=naverClientSecret

# 저장: Ctrl+O, Enter, Ctrl+X

# 5단��: 파일 보호
chmod 600 .env

# 6단계: 환경변수 확인
chmod +x scripts/check-env.sh
./scripts/check-env.sh

# 7단계: 빌드 및 실행
./gradlew clean build -x test
export $(cat .env | xargs)
nohup java -jar build/libs/blitz-*.jar > logs/app.log 2>&1 &

# 8단계: 실행 확인
sleep 10
curl http://localhost:8080/actuator/health

# 9단계: 로그 확인
tail -f logs/app.log
```

---

## 🔧 문제 해결

### 문제 1: 환경변수가 로드되지 않음

```bash
# 증상: 애플리케이션이 환경변수를 못 찾음

# 해결 1: .env 파일 확인
cat .env | grep DB_PASSWORD

# 해결 2: 환경변수 수동 로드
export $(cat .env | grep -v '^#' | xargs)

# 해결 3: Spring Boot에 직접 전달
java -jar blitz.jar \
  --spring.datasource.password=$DB_PASSWORD \
  --spring.security.oauth2.client.registration.google.client-id=$GOOGLE_CLIENT_ID
```

### 문제 2: 데이터베이스 연결 실패

```bash
# DB 서버 실행 확인
sudo systemctl status mariadb

# DB 접속 테스트
mysql -h localhost -u blitz_user -p

# 방화벽 확인
sudo ufw status
sudo ufw allow 3306/tcp
```

### 문제 3: Redis 연결 실패

```bash
# Redis 실행 확인
sudo systemctl status redis

# Redis 접속 테스트
redis-cli ping

# 비밀번호가 있는 경우
redis-cli -a your_redis_password ping
```

---

## 📚 추가 자료

### 전체 가이드
상세한 내용은 [DEPLOYMENT_GUIDE.md](../DEPLOYMENT_GUIDE.md)를 참조하세요.

### 빠른 명령어 참고

```bash
# 환경변수 확인
./scripts/check-env.sh

# 자동 배포 (로컬에서 실행)
./scripts/deploy-to-server.sh

# 서비스 관리
sudo systemctl start blitz    # 시작
sudo systemctl stop blitz     # 중지
sudo systemctl restart blitz  # 재시작
sudo systemctl status blitz   # 상태 확인

# 로그 확인
sudo journalctl -u blitz -f   # systemd 로그
tail -f logs/app.log          # 애플리케이션 로그
```

---

## 💡 팁

### 1. 비밀번호 강도 높이기

```bash
# 32자 랜덤 비밀번호 생성
openssl rand -base64 32

# UUID 생성
uuidgen
```

### 2. 여러 환경 관리

```bash
# 개발 환경
cp .env .env.dev
nano .env.dev

# 운영 환경
cp .env .env.prod
nano .env.prod

# 사용 시
export $(cat .env.prod | xargs)
```

### 3. 환경변수 백업

```bash
# 암호화해서 백업
tar -czf env-backup.tar.gz .env
openssl enc -aes-256-cbc -salt -in env-backup.tar.gz -out env-backup.tar.gz.enc
rm env-backup.tar.gz

# 복원
openssl enc -aes-256-cbc -d -in env-backup.tar.gz.enc -out env-backup.tar.gz
tar -xzf env-backup.tar.gz
```

---

**🔒 기억하세요**: 민감정보는 절대 Git에 커밋하지 마세요!

