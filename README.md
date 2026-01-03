# Pdf-Assembly-Showcase
A Spring Boot microservice for generating PDFs from HTML and signing them using *EU DSS* (profile: pades-lt).
The pdf generation can be done by
* Create the pdf in-app using [Playwright Java](https://playwright.dev/java/docs/intro). Supports headless Chromium execution, pre-installed browser binaries, is optimized for Docker and local development.
* Create the pdf by using self-hosted [Gotenberg Service](https://gotenberg.dev).

<p align="center" width="100%">
<img src="docs/images/pdfPreview.png" alt="pdf preview" style="width: 85%;">
</p>

---

## Features

- Generate PDFs via Playwright from HTML templates via thymeleafe
- Headless Chromium support (new headless mode)
- Pre-download browsers at build time to avoid runtime downloads
- Optimzed Dockerfile for readonly environments
- Signing of PDF's in compliance with ISO 32000-1, eIDAS, AdES (PAdES-LT), BSI TR-03138 – RESISCAN
- Generate PDFs via seperate Gotenberg service from HTML templates via thymeleafe
- Restrict access to Gotenberg service via Basic Auth
- Include images into pdf by mount them directly into Gotenberg (see docker compose) or send them via http

---

## Requirements

- Java 25+
- Gradle 9.2+
- macOS, Linux, or Windows
- Optional: Docker & Docker Compose for containerized builds/execs

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

## Build & Run: Standalone

Build the project (will run installPlaywrightChromium first):
```bash
  ./gradlew build
```

Run local:
```bash
  ./gradlew bootRun
```

Download PDF (created internally)
```bash
  open http://localhost:8080/create-pdf
```

## Build & Run: Docker Compose

Build the project:
```bash
  docker compose build
```

Run local:
```bash
  docker compose up -d
```

Test Gotenberg Service:
```bash
  echo "<html><body><h1>Hello Gotenberg</h1></body></html>" > index.html;
  curl -X POST "http://0.0.0.0:3000/forms/chromium/convert/html" -F "files[]=@index.html" --output output.pdf
```

Download PDF (created by gotenberg)
```bash
  open http://localhost:8081/create-pdf-with-gotenberg
```

## IntelliJ Setup

1. Open run config
2. Add before launch gradle task: installPlaywrightChromium

## Build with

- java v25
- spring boot v4
- thymeleafe v1.57
- gotenberg v8.25


## Signing
The signing is done via [EU Digital Signature Service (DSS)](https://github.com/esig/dss).
The signing requires the following input (here for PDF signature level "pades" in profile "lt")

1. PKCE#12 File (see self-signed signing.p12): Bundles Signing Certificate (X.509) + its private key + intermediate ca + root ca (trust-chain has to be complete)
2. Input pdf (in bytes)
3. Timestamp provider (TSA-Source): For testing purposes set to "http://timestamp.digicert.com"
4. CertificateVerifier (OCSP / CRL Fetching): DSS will read the AIA-URLs for OCSP directly from the X.509-certificate (see first requirement)

The input parameters are defined in apps application.yaml.
The signature of the output-pdf can be validated by [DSS Validator WebApp](https://ec.europa.eu/digital-building-blocks/DSS/webapp-demo/validation)

<p align="center" width="100%">
<img src="docs/images/dssValidation.png" alt="pdf preview" style="width: 85%;">
</p>

Further fundamentals are described in [pdfSigning.pdf](docs/pdfSigning.pdf)

### Generate self-signed certificate
The self-signed certificate can be generated via following snipped:

```bash
  keytool
    -genkeypair \
    -alias test-signing \
    -keyalg RSA \
    -keysize 2048 \
    -sigalg SHA256withRSA \
    -keystore signing.p12 \
    -storetype PKCS12 \
    -validity 3650 \
    -storepass password \
    -keypass password \
    -dname "CN=Test User, OU=PoC, O=Example, L=Berlin, C=DE"
```

## Additional hints

* Why dont use the official playwright docker image (see [docs](https://playwright.dev/java/docs/docker))?
    * Docu states "This Docker image is intended to be used for testing and development purposes only. It is not recommended to use this Docker image to visit untrusted websites."
    * Given docker image does not allow to specify java version
* Gotenberg project does not provide an official openapi-spec which would allow auto-generated (java) clients. Yes there are community SDK's but especially for java these are rather not mature or getting less maintenance. The approache here is to hard-code the concrete endpoint and input/output objects (if even needed). Since the communicated data are mostly byte-array this is accepted here.