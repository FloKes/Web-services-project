#!/bin/bash
set -e

pushd simple-dtu-pay-server
mvn clean package

# Create a new docker image if necessary.
# Restarts the container with the new image if necessary
# The server stays running.
# To terminate the server run docker-compose down in the
# code-with-quarkus direcgtory
docker-compose up -d --build
# clean up images
docker image prune -f

popd

# Give the Web server a chance to finish start up
sleep 2

pushd simple-dtu-pay-client
mvn test
popd

pushd simple-dtu-pay-server
docker-compose down
popd

