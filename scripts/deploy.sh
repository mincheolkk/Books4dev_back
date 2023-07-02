#!/bin/bash

export DOCKER_ID=''
export DOCKER_PASSWORD=''

# 도커 로그인
echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_ID} --password-stdin

# 가동중인 books4dev 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=books4dev" | grep -q . && docker stop books4dev && docker rm books4dev | true

# 기존 이미지 삭제
sudo docker rmi ${DOCKER_ID}/books4dev-dockerhub

# 도커허브 이미지 pull
sudo docker pull ${DOCKER_ID}/books4dev-dockerhub

# 도커 run
docker run -d -p 8080:8080 -e TZ=Asia/Seoul -v /home/ubuntu/logs:/logs --name books4dev ${DOCKER_ID}/books4dev-dockerhub:1.0
