Write-Host "Starting ECommerce Platform..." -ForegroundColor Green
docker-compose up -d
Write-Host "All services started!" -ForegroundColor Green
docker-compose ps