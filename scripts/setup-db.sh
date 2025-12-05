#!/bin/bash
# MariaDB 및 Redis 초기 설정 스크립트

set -e

echo "========================================="
echo "MariaDB & Redis 설치 및 설정"
echo "========================================="

# 1. 패키지 업데이트
echo "1. 패키지 업데이트..."
sudo apt update

# 2. MariaDB 설치
echo "2. MariaDB 설치..."
if ! command -v mysql &> /dev/null; then
    sudo apt install -y mariadb-server
    sudo systemctl start mariadb
    sudo systemctl enable mariadb
    echo "✅ MariaDB 설치 완료"
else
    echo "✅ MariaDB 이미 설치됨"
fi

# 3. Redis 설치
echo "3. Redis 설치..."
if ! command -v redis-cli &> /dev/null; then
    sudo apt install -y redis-server
    sudo systemctl start redis
    sudo systemctl enable redis
    echo "✅ Redis 설치 완료"
else
    echo "✅ Redis 이미 설치됨"
fi

# 4. DB 생성
echo ""
echo "4. 데이터베이스 설정..."
echo ""
read -p "DB 비밀번호를 입력하세요: " -s DB_PASS
echo ""

sudo mysql <<EOF
-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS kraft_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 (이미 존재하면 무시)
CREATE USER IF NOT EXISTS 'kraft_user'@'localhost' IDENTIFIED BY '$DB_PASS';

-- 권한 부여
GRANT ALL PRIVILEGES ON kraft_db.* TO 'kraft_user'@'localhost';

-- 권한 적용
FLUSH PRIVILEGES;

-- 확인
SELECT User, Host FROM mysql.user WHERE User='kraft_user';
SHOW DATABASES LIKE 'kraft_db';
EOF

echo ""
echo "✅ 데이터베이스 설정 완료"
echo ""

# 5. 연결 테스트
echo "5. 연결 테스트..."
if mysql -u kraft_user -p"$DB_PASS" kraft_db -e "SELECT 1;" &> /dev/null; then
    echo "✅ DB 연결 성공"
else
    echo "❌ DB 연결 실패"
    exit 1
fi

if redis-cli ping &> /dev/null; then
    echo "✅ Redis 연결 성공"
else
    echo "❌ Redis 연결 실패"
fi

echo ""
echo "========================================="
echo "설정 완료!"
echo "========================================="
echo ""
echo "다음 단계:"
echo "1. .env 파일을 생성하세요:"
echo "   cp .env.example .env"
echo "   nano .env"
echo ""
echo "2. .env 파일에 다음 값을 입력하세요:"
echo "   DB_PASSWORD=$DB_PASS"
echo "   REDIS_PASSWORD=(비밀번호가 있다면)"
echo ""
echo "3. 애플리케이션 실행:"
echo "   ./scripts/run-server.sh"
echo ""

