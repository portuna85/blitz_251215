#!/bin/bash
# 서버 초기 설정 및 실행 스크립트

set -e

echo "========================================="
echo "Blitz 서버 설정 및 실행"
echo "========================================="

# 1. 권한 설정
echo "1. 실행 권한 설정..."
chmod +x gradlew
chmod +x scripts/*.sh 2>/dev/null || true

# 2. .env 파일 확인
if [ ! -f .env ]; then
    echo ""
    echo "❌ .env 파일이 없습니다!"
    echo ""
    echo "다음 중 하나를 선택하세요:"
    echo ""
    echo "옵션 1: .env 파일 생성 (운영 환경)"
    echo "  cp .env.example .env"
    echo "  nano .env"
    echo "  # DB_PASSWORD, REDIS_PASSWORD 등 실제 값 입력"
    echo "  chmod 600 .env"
    echo ""
    echo "옵션 2: H2 DB로 빠른 테스트 (개발 환경)"
    echo "  SPRING_PROFILES_ACTIVE=local ./gradlew bootRun"
    echo ""
    echo "옵션 3: Docker로 MariaDB/Redis 실행"
    echo "  docker-compose up -d mariadb redis"
    echo "  # .env 파일 생성 후 실행"
    echo ""
    exit 1
fi

# 3. MariaDB 확인
echo "2. MariaDB 확인..."
if ! systemctl is-active --quiet mariadb 2>/dev/null; then
    echo "⚠️  MariaDB가 실행되지 않았습니다."
    echo "   설치: sudo apt install -y mariadb-server"
    echo "   시작: sudo systemctl start mariadb"
    echo ""
    read -p "H2로 실행하시겠습니까? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo "H2 모드로 실행합니다..."
        SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
        exit 0
    else
        exit 1
    fi
fi

# 4. Redis 확인
echo "3. Redis 확인..."
if ! systemctl is-active --quiet redis 2>/dev/null; then
    echo "⚠️  Redis가 실행되지 않았습니다."
    echo "   설치: sudo apt install -y redis-server"
    echo "   시작: sudo systemctl start redis"
fi

# 5. 환경변수 로드
echo "4. 환경변수 로드..."
export $(cat .env | grep -v '^#' | grep -v '^$' | xargs)

# 6. 환경변수 확인
echo "5. 필수 환경변수 확인..."
MISSING=0

if [ -z "$DB_PASSWORD" ]; then
    echo "❌ DB_PASSWORD가 설정되지 않았습니다!"
    MISSING=1
fi

if [ -z "$REDIS_PASSWORD" ]; then
    echo "⚠️  REDIS_PASSWORD가 설정되지 않았습니다 (선택사항)"
fi

if [ $MISSING -eq 1 ]; then
    echo ""
    echo ".env 파일을 확인하고 필수 값을 입력하세요:"
    echo "  nano .env"
    exit 1
fi

echo "✅ 환경변수 확인 완료"

# 7. 실행
echo ""
echo "========================================="
echo "애플리케이션 실행 중..."
echo "========================================="
echo ""

./gradlew bootRun

