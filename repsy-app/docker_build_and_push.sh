#!/usr/bin/env bash
set -e

REPO_NAME=repo.repsy.io/bernabaris/repsy

mvn clean -DskipTests=true install
docker build -f docker/Dockerfile -t $REPO_NAME/repsy-app:1.0.0 .
docker login $REPO_NAME
sudo docker push $REPO_NAME/repsy-app:1.0.0
