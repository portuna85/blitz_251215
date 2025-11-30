# GitHub 리포지토리 생성 및 푸시 가이드

## 현재 상태
✅ Git 저장소 초기화 완료
✅ 파일 커밋 완료 (66개 파일)
✅ 원격 리포지토리 설정 완료
⏳ GitHub에 리포지토리 생성 필요

## 1단계: GitHub에서 리포지토리 생성

### 방법 1: 웹 브라우저 사용 (추천)

1. https://github.com/new 접속
2. 리포지토리 설정:
   - **Repository name**: `blitz`
   - **Description**: Spring Boot application with OAuth2, JPA, Redis Session
   - **Visibility**: Public 또는 Private 선택
   - **⚠️ 중요**: "Add a README file", ".gitignore", "license" 옵션 모두 **체크 해제** (이미 로컬에 있음)
3. "Create repository" 클릭

### 방법 2: GitHub CLI 사용 (설치 필요)

GitHub CLI를 설치하려면:
```powershell
winget install --id GitHub.cli
```

설치 후:
```powershell
gh auth login
gh repo create blitz --public --source=. --remote=origin --push
```

## 2단계: 코드 푸시

웹 브라우저로 리포지토리를 생성한 경우, 아래 명령어를 실행하세요:

```powershell
cd D:\workspace\spring\blitz
git push -u origin main
```

## 확인 사항

푸시 완료 후 https://github.com/portuna85/blitz 에서 확인하세요.

### 커밋된 파일 (66개):
- ✅ 소스 코드 (.java)
- ✅ 설정 파일 (build.gradle, application.yml)
- ✅ 테스트 코드
- ✅ 리소스 파일 (templates, static)
- ✅ 배포 스크립트 (scripts/)
- ✅ README.md

### 제외된 파일 (민감정보):
- ❌ .env (환경변수)
- ❌ secret/ (OAuth 시크릿 파일)
- ❌ logs/ (로그 파일)
- ❌ application-oauth.yml (OAuth 설정)
- ❌ application-real-db.yml (DB 설정)
- ❌ build/ (빌드 산출물)

## 문제 해결

### 푸시 오류 발생 시

1. **인증 오류**:
   ```powershell
   git config --global credential.helper manager-core
   ```

2. **원격 리포지토리 확인**:
   ```powershell
   git remote -v
   ```

3. **원격 리포지토리 재설정**:
   ```powershell
   git remote remove origin
   git remote add origin https://github.com/portuna85/blitz.git
   git push -u origin main
   ```

## 다음 단계

푸시 완료 후:
1. GitHub Actions 설정 (CI/CD)
2. Branch protection rules 설정
3. Issue/PR 템플릿 확인
4. README.md 업데이트

