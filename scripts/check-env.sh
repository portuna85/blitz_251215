#!/bin/bash
# ==================================
# 환경변수 확인 스크립트
# ==================================

echo "======================================"
echo "🔍 환경변수 확인"
echo "======================================"

# 색상 정의
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

check_env() {
    local var_name=$1
    local var_value="${!var_name}"

    if [ -z "$var_value" ]; then
        echo -e "${RED}❌ $var_name: 설정되지 않음${NC}"
        return 1
    else
        # 민감정보는 일부만 표시
        if [[ $var_name == *"PASSWORD"* ]] || [[ $var_name == *"SECRET"* ]] || [[ $var_name == *"KEY"* ]]; then
            local masked_value="${var_value:0:3}***${var_value: -3}"
            echo -e "${GREEN}✅ $var_name: $masked_value${NC}"
        else
            echo -e "${GREEN}✅ $var_name: $var_value${NC}"
        fi
        return 0
    fi
}

# .env 파일 로드
if [ -f ".env" ]; then
    echo -e "${GREEN}📄 .env 파일을 찾았습니다.${NC}"
    export $(cat .env | grep -v '^#' | xargs)
else
    echo -e "${YELLOW}⚠️  .env 파일이 없습니다. 시스템 환경변수를 확인합니다.${NC}"
fi

echo ""
echo "======================================"
echo "서버 설정"
echo "======================================"
check_env "SERVER_PORT"
check_env "SPRING_PROFILES_ACTIVE"

echo ""
echo "======================================"
echo "데이터베이스 설정"
echo "======================================"
check_env "DB_HOST"
check_env "DB_PORT"
check_env "DB_NAME"
check_env "DB_USER"
check_env "DB_PASSWORD"

echo ""
echo "======================================"
echo "Redis 설정"
echo "======================================"
check_env "REDIS_HOST"
check_env "REDIS_PORT"
check_env "REDIS_PASSWORD"

echo ""
echo "======================================"
echo "OAuth2 설정"
echo "======================================"
check_env "GOOGLE_CLIENT_ID"
check_env "GOOGLE_CLIENT_SECRET"
check_env "NAVER_CLIENT_ID"
check_env "NAVER_CLIENT_SECRET"

echo ""
echo "======================================"
echo "MinIO 설정"
echo "======================================"
check_env "MINIO_ENDPOINT"
check_env "MINIO_ACCESS_KEY"
check_env "MINIO_SECRET_KEY"
check_env "MINIO_BUCKET_NAME"

echo ""
echo "======================================"
echo "🔌 연결 테스트"
echo "======================================"

# 데이터베이스 연결 테스트
if command -v mysql &> /dev/null; then
    echo -n "Database 연결: "
    if mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1;" &> /dev/null; then
        echo -e "${GREEN}✅ 성공${NC}"
    else
        echo -e "${RED}❌ 실패${NC}"
    fi
fi

# Redis 연결 테스트
if command -v redis-cli &> /dev/null; then
    echo -n "Redis 연결: "
    if [ -n "$REDIS_PASSWORD" ]; then
        if redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" -a "$REDIS_PASSWORD" ping &> /dev/null; then
            echo -e "${GREEN}✅ 성공${NC}"
        else
            echo -e "${RED}❌ 실패${NC}"
        fi
    else
        if redis-cli -h "$REDIS_HOST" -p "$REDIS_PORT" ping &> /dev/null; then
            echo -e "${GREEN}✅ 성공${NC}"
        else
            echo -e "${RED}❌ 실패${NC}"
        fi
    fi
fi

echo ""
echo "======================================"

