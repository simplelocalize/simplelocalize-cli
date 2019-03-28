#!/usr/bin/env bash

echo "Running SimpleLocalize-CLI..."

# install docker if not exists

codePath=$1
echo "Search path: ${codePath}"

mkdir .tmp_src/
cp -rf ${codePath} .tmp_src/

CONTAINER_NAME=simplelocalize-processor

docker build -t simplelocalize --no-cache --force-rm  . > /dev/null

docker stop ${CONTAINER_NAME}  > /dev/null
docker rm ${CONTAINER_NAME} > /dev/null

docker run --name=${CONTAINER_NAME} simplelocalize

docker stop ${CONTAINER_NAME} > /dev/null
docker rm ${CONTAINER_NAME} > /dev/null

rm -rf .tmp_src/
