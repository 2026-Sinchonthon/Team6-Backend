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
set -euo pipefail

HOST="${SINCHONTON_HOST:-43.201.9.6}"
SSH_KEY="${SINCHONTON_KEY:-deploy/sinchonton-key.pem}"
REMOTE_DIR="/opt/sinchonton"

cd "$(dirname "$0")/.."

echo "▶ 1/4  jar 빌드"
./gradlew bootJar -x test --no-daemon -q
JAR=$(ls -t build/libs/*.jar | grep -v plain | head -1)
echo "   $JAR ($(du -h "$JAR" | cut -f1))"

echo "▶ 2/4  파일 전송 → $HOST"
ssh -o StrictHostKeyChecking=no -i "$SSH_KEY" "ubuntu@$HOST" "mkdir -p $REMOTE_DIR"
scp -o StrictHostKeyChecking=no -i "$SSH_KEY" \
    "$JAR" "ubuntu@$HOST:$REMOTE_DIR/app.jar"
scp -o StrictHostKeyChecking=no -i "$SSH_KEY" \
    deploy/docker-compose.prod.yml "ubuntu@$HOST:$REMOTE_DIR/"

echo "▶ 3/4  컨테이너 재시작"
ssh -o StrictHostKeyChecking=no -i "$SSH_KEY" "ubuntu@$HOST" \
    "cd $REMOTE_DIR && docker compose -f docker-compose.prod.yml --env-file .env.prod up -d --remove-orphans"

echo "▶ 4/4  헬스체크"
for i in $(seq 1 30); do
    if curl -sf "http://$HOST:8080/api/health" > /dev/null 2>&1; then
        echo "   ✅ http://$HOST:8080/api/health 정상"
        exit 0
    fi
    sleep 5
done

echo "   ❌ 헬스체크 실패. 로그를 확인하세요:"
echo "      ssh -i $SSH_KEY ubuntu@$HOST 'cd $REMOTE_DIR && docker compose -f docker-compose.prod.yml logs --tail=80 app'"
exit 1
