#!/bin/bash
set -e
docker image prune -f
docker-compose up -d rabbitMq
sleep 10
docker-compose up -d dtu-pay-service payment-service account-service token-service report-service

