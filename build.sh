#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd messaging-utilities-3.2
./build.sh
popd 

# Build the services
pushd dtu-pay-service
./build.sh
popd

pushd payment-service
./build.sh
popd

pushd account-service
./build.sh
popd

