#!/bin/bash
# ==================================
# 서버 환경변수 설정 스크립트
# ==================================
# 사용법: source set-env.sh 또는 . set-env.sh
# ==================================

# ===== 서버 설정 =====
export SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=real

# ===== 데이터베이스 설정 =====
export DB_HOST="localhost"
export DB_PORT="3306"
export DB_NAME="blitz_db"
export DB_USER="blitz_user"
export DB_PASSWORD="CHANGE_THIS_PASSWORD"
export DB_TIMEZONE="Asia/Seoul"

# ===== Redis 설정 =====
export REDIS_HOST="localhost"
export REDIS_PORT="6379"
export REDIS_PASSWORD="CHANGE_THIS_PASSWORD"

# ===== OAuth2 - Google =====
export GOOGLE_CLIENT_ID="your-google-client-id.apps.googleusercontent.com"
export GOOGLE_CLIENT_SECRET="your-google-client-secret"

# ===== OAuth2 - Naver =====
export NAVER_CLIENT_ID="your-naver-client-id"
export NAVER_CLIENT_SECRET="your-naver-client-secret"

# ===== MinIO 설정 =====
export MINIO_ENDPOINT="http://localhost:9000"
export MINIO_ACCESS_KEY="your-minio-access-key"
export MINIO_SECRET_KEY="your-minio-secret-key"
export MINIO_BUCKET_NAME="blitz-bucket"

echo "✅ 환경변수가 설정되었습니다."
echo "현재 프로파일: $SPRING_PROFILES_ACTIVE"

