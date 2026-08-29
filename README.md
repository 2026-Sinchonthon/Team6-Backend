# Team6-Backend

2026 신촌톤 6팀 백엔드

## 기술 스택

- Java 21
- Spring Boot 4.0.8-SNAPSHOT (Gradle, `repo.spring.io/snapshot` 저장소 사용)
- Spring Data JPA
- MySQL (배포), H2 (로컬)

## 로컬에서 실행하기

기본 프로필은 `local`이고, 별도 DB 설치 없이 인메모리 H2로 뜹니다.

```bash
./gradlew bootRun
```

- 헬스체크: http://localhost:8080/api/health
- H2 콘솔: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:backend`)

## 프로젝트 구조

```
src/main/java/com/sinchonton/backend/
  BackendApplication.java
  controller/          # REST 컨트롤러
src/main/resources/
  application.yml        # 공통 설정 (기본 profile: local)
  application-local.yml   # 로컬 개발 (H2)
  application-prod.yml    # 배포 (MySQL, 환경변수로 값 주입)
```

## 배포

EC2(Ubuntu, Docker) 위에 `docker compose`로 mysql + app 컨테이너를 띄우는 구조입니다.
도메인이 없어 `http://<서버 IP>:8080`으로 직접 접속합니다 (HTTPS 없음).

### 사전 준비

1. `deploy/sinchonton-key.pem` — 팀 내부에서 안전하게 공유받아 저장소 루트의 `deploy/` 아래 위치 (저장소에는 커밋 안 됨, `.gitignore`에 포함)
2. 서버(`/opt/sinchonton/.env.prod`)에 아래 값이 채워져 있어야 함 (`deploy/.env.prod.example` 참고):
   ```
   MYSQL_ROOT_PASSWORD=...
   MYSQL_PASSWORD=...
   JWT_SECRET=...
   KAKAO_CLIENT_ID=...
   KAKAO_CLIENT_SECRET=...
   OAUTH2_REDIRECT_URI=...
   OAUTH2_ALLOWED_REDIRECT_ORIGINS=...
   CORS_ALLOWED_ORIGINS=...
   ```

### 배포 실행

```bash
./deploy/deploy.sh
```

로컬에서 jar를 빌드 → scp로 서버 전송 → `docker compose up -d` → 헬스체크까지 자동으로 진행됩니다.
서버 주소를 바꾸고 싶으면 환경변수로 덮어쓸 수 있습니다.

```bash
SINCHONTON_HOST=<IP> SINCHONTON_KEY=<pem경로> ./deploy/deploy.sh
```

### 서버 구성 (`deploy/docker-compose.prod.yml`)

| 컨테이너 | 역할 |
|---|---|
| `sinchonton-app` | Spring Boot 앱. 로컬에서 만든 jar를 마운트해서 실행 (서버에서 직접 빌드 안 함) |
| `sinchonton-mysql` | MySQL 8.4. 데이터는 named volume(`mysql-data`)에 보존 |

### 로그 확인 / 문제 해결

```bash
ssh -i deploy/sinchonton-key.pem ubuntu@<서버IP> \
  'cd /opt/sinchonton && docker compose -f docker-compose.prod.yml logs --tail=80 app'
```

## AWS 인프라

- EC2 t3.small (Ubuntu 24.04), 리전: `ap-northeast-2`
- 보안그룹: 22(SSH), 80, 8080 전체 오픈
- 해커톤 당일 데모용으로, 종료 후 인스턴스는 폐기 예정
