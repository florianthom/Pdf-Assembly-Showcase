# Pdf-Assembly-Showcase
A Spring Boot microservice for generating PDFs from HTML using **Playwright Java**.  
Supports headless Chromium execution, pre-installed browser binaries, and is optimized for CI/CD, Docker, and local development.

---

## Features

- Generate PDFs from HTML templates via Playwright
- Headless Chromium support (new headless mode)
- Pre-download browsers at build time to avoid runtime downloads
- Compatible with Docker and restricted environments
- Ready for integration tests with Playwright

---

## Requirements

- Java 25+
- Gradle 9.2+
- macOS, Linux, or Windows
- Optional: Docker for containerized builds

---

## Gradle Tasks

### 1. Install Playwright Chromium

Installs Playwright-managed Chromium (with OS dependencies) to the local cache:

```bash
./gradlew installPlaywrightChromium
```

### 2. Uninstall Playwright Chromium

Uninstalls all Playwright-managed browsers (with OS dependencies) to the local cache:

```bash
./gradlew uninstallPlaywrightChromium
```
## Build & run

Build the project (will run installPlaywrightChromium first):
```bash
./gradlew build
```

Run locally:
```bash
./gradlew bootRun
```

## IntelliJ Setup

1. Open run config
2. Add before launch gradle task: installPlaywrightChromium

## Build with

- java v25
- spring boot v4
- thymeleafe v1.57