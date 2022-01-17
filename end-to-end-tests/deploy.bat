call docker image prune -f
call docker-compose up -d rabbitMq
call timeout /t 10
call docker-compose up -d dtu-pay-service account-service