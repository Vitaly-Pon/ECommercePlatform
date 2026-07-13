$ErrorActionPreference = "Stop"

$secretDir = ".secrets/dev"

New-Item -ItemType Directory -Force -Path $secretDir | Out-Null

$privateKey = "$secretDir/private.key"
$publicKey = "$secretDir/public.key"

if ((Test-Path $privateKey) -and (Test-Path $publicKey)) {
    Write-Host "RSA keys already exist. Generation skipped."
    exit 0
}

Write-Host "Generating RSA private key..."

openssl genpkey `
  -algorithm RSA `
  -pkeyopt rsa_keygen_bits:2048 `
  -out $privateKey

Write-Host "Generating RSA public key..."

openssl rsa `
  -pubout `
  -in $privateKey `
  -out $publicKey

Write-Host "RSA key pair generated successfully."