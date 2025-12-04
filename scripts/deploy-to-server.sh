#!/bin/bash
# ==================================
# 서버 배포 스크립트
# ==================================

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 설정
SERVER_USER="steve"
SERVER_HOST="192.168.0.9"
SERVER_DIR="/home/steve/blitz"
APP_NAME="blitz"

echo -e "${GREEN}🚀 서버 배포 시작...${NC}"

# 1. 환경변수 파일 확인
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}⚠️  .env 파일이 없습니다. .env.example을 참고하여 생성하세요.${NC}"
    read -p "계속하시겠습니까? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# 2. 로컬 빌드
echo -e "${GREEN}📦 빌드 중...${NC}"
./gradlew clean build -x test

# 3. JAR 파일 확인
JAR_FILE=$(ls build/libs/${APP_NAME}-*.jar | grep -v plain | head -n 1)
if [ -z "$JAR_FILE" ]; then
    echo -e "${RED}❌ JAR 파일을 찾을 수 없습니다.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ 빌드 완료: $JAR_FILE${NC}"

# 4. 서버에 디렉토리 생성
echo -e "${GREEN}📁 서버 디렉토리 준비 중...${NC}"
ssh ${SERVER_USER}@${SERVER_HOST} "mkdir -p ${SERVER_DIR}"

# 5. JAR 파일 전송
echo -e "${GREEN}📤 JAR 파일 전송 중...${NC}"
scp $JAR_FILE ${SERVER_USER}@${SERVER_HOST}:${SERVER_DIR}/${APP_NAME}.jar

# 6. .env 파일 전송 (선택사항)
if [ -f ".env" ]; then
    read -p "🔐 .env 파일을 서버에 전송하시겠습니까? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        scp .env ${SERVER_USER}@${SERVER_HOST}:${SERVER_DIR}/.env
        ssh ${SERVER_USER}@${SERVER_HOST} "chmod 600 ${SERVER_DIR}/.env"
        echo -e "${GREEN}✅ .env 파일 전송 완료${NC}"
    fi
fi

# 7. 서버에서 애플리케이션 재시작
echo -e "${GREEN}🔄 애플리케이션 재시작 중...${NC}"
ssh ${SERVER_USER}@${SERVER_HOST} << EOF
    cd ${SERVER_DIR}

    # 기존 프로세스 종료
    if pgrep -f "${APP_NAME}.jar" > /dev/null; then
        echo "기존 프로세스 종료 중..."
        pkill -f "${APP_NAME}.jar" || true
        sleep 3
    fi

    # 환경변수 로드 및 실행
    if [ -f .env ]; then
        export \$(cat .env | grep -v '^#' | xargs)
    fi

    # 백그라운드로 실행
    nohup java -jar ${APP_NAME}.jar > logs/app.log 2>&1 &

    echo "애플리케이션이 시작되었습니다."
    sleep 5

    # 프로세스 확인
    if pgrep -f "${APP_NAME}.jar" > /dev/null; then
        echo "✅ 애플리케이션이 정상적으로 실행 중입니다."
        echo "PID: \$(pgrep -f ${APP_NAME}.jar)"
    else
        echo "❌ 애플리케이션 시작 실패"
        tail -n 50 logs/app.log
        exit 1
    fi
EOF

echo -e "${GREEN}✅ 배포 완료!${NC}"
echo -e "${YELLOW}📊 로그 확인: ssh ${SERVER_USER}@${SERVER_HOST} 'tail -f ${SERVER_DIR}/logs/app.log'${NC}"

