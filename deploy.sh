#!/bin/bash

PROJECT_ROOT=$(pwd)
ENV_FILE=$PROJECT_ROOT/.env

git checkout step3
git fetch origin
git reset --hard origin/step3

./gradlew clean build -x test

BUILD_PATH=$(ls $PROJECT_ROOT/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_PATH)

CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]
then
  sleep 1
else
  kill -15 $CURRENT_PID
  sleep 5
fi

if [ -f "$ENV_FILE" ]; then
  set -a
  source "$ENV_FILE"
  set +a
fi

cp $BUILD_PATH $PROJECT_ROOT

nohup java -jar $PROJECT_ROOT/$JAR_NAME > app.out.log 2> app.err.log &
