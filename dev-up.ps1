Write-Host "Starting ECommerce Platform..." -ForegroundColor Green

docker compose up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "Startup failed!" -ForegroundColor Red
    exit 1
}

Write-Host "All services started!" -ForegroundColor Green

docker compose ps