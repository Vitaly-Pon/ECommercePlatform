# Выводит жёлтый текст
Write-Host "Stopping ECommerce Platform..." -ForegroundColor Yellow

# Останавливает и удаляет все контейнеры
docker-compose down

# Выводит сообщение
Write-Host "All services stopped!" -ForegroundColor Yellow