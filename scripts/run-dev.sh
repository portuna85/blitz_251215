#!/bin/bash
# H2 DB로 빠른 실행 (개발 모드)

echo "========================================="
echo "H2 개발 모드로 실행"
echo "========================================="
echo ""
echo "주의: 이 모드는 개발/테스트용입니다."
echo "운영 환경에서는 MariaDB를 사용하세요."
echo ""

# 권한 설정
chmod +x gradlew 2>/dev/null || true

# H2 모드로 실행
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun

