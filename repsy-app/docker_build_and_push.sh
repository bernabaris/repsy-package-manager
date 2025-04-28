#!/usr/bin/env bash
set -e

REPO_NAME=repo.repsy.io/bernabaris/repsy

mvn clean -DskipTests=true install
docker build --platform linux/amd64 -f docker/Dockerfile -t $REPO_NAME/repsy-app:1.0.0 .
docker login $REPO_NAME
sudo docker push --disable-content-trust --platform linux/amd64 $REPO_NAME/repsy-app:1.0.0
