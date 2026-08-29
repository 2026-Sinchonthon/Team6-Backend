#!/bin/bash
# sinchonton 백엔드 배포 스크립트
#
#   ./deploy/deploy.sh
#
# 로컬에서 jar 를 빌드해 EC2 로 올리고 컨테이너를 재시작합니다.
#
# 사전 준비
#   - deploy/sinchonton-key.pem 이 저장소에 있을 것 (커밋 금지)
#   - 서버 /opt/sinchonton/.env.prod 에 비밀값이 채워져 있을 것
#   - api.sintime.site 의 A 레코드가 EC2 IP 를 가리키고 있을 것 (Caddy 인증서 발급 조건)
set -euo pipefail

SSH_HOST="${SINCHONTON_SSH_HOST:-43.201.9.6}"
API_HOST="${SINCHONTON_API_HOST:-api.sintime.site}"
SSH_KEY="${SINCHONTON_KEY:-deploy/sinchonton-key.pem}"
REMOTE_DIR="/opt/sinchonton"

cd "$(dirname "$0")/.."

echo "▶ 1/4  jar 빌드"
./gradlew bootJar -x test --no-daemon -q
JAR=$(ls -t build/libs/*.jar | grep -v plain | head -1)
echo "   $JAR ($(du -h "$JAR" | cut -f1))"

echo "▶ 2/4  파일 전송 → $SSH_HOST"
ssh -o StrictHostKeyChecking=no -i "$SSH_KEY" "ubuntu@$SSH_HOST" "mkdir -p $REMOTE_DIR"
scp -o StrictHostKeyChecking=no -i "$SSH_KEY" \
    "$JAR" "ubuntu@$SSH_HOST:$REMOTE_DIR/app.jar"
scp -o StrictHostKeyChecking=no -i "$SSH_KEY" \
    deploy/docker-compose.prod.yml deploy/Caddyfile "ubuntu@$SSH_HOST:$REMOTE_DIR/"

echo "▶ 3/4  컨테이너 재시작"
# app.jar 는 이미지에 안 들어있고 호스트 파일을 그대로 마운트하는 구조라,
# 파일만 바꿔서는 docker compose 가 변경을 감지하지 못합니다. --force-recreate 로 항상 재시작합니다.
ssh -o StrictHostKeyChecking=no -i "$SSH_KEY" "ubuntu@$SSH_HOST" \
    "cd $REMOTE_DIR && docker compose -f docker-compose.prod.yml --env-file .env.prod up -d --remove-orphans && \
     docker compose -f docker-compose.prod.yml --env-file .env.prod up -d --force-recreate --no-deps app"

echo "▶ 4/4  헬스체크"
for i in $(seq 1 30); do
    if curl -sf "https://$API_HOST/api/health" > /dev/null 2>&1; then
        echo "   ✅ https://$API_HOST/api/health 정상"
        exit 0
    fi
    sleep 5
done

echo "   ❌ 헬스체크 실패. 로그를 확인하세요:"
echo "      ssh -i $SSH_KEY ubuntu@$SSH_HOST 'cd $REMOTE_DIR && docker compose -f docker-compose.prod.yml logs --tail=80 caddy app'"
exit 1
