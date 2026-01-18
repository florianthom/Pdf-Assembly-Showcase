plugins {
	java
	id("org.springframework.boot") version "4.0.1-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.florianthom"
version = "0.0.1-SNAPSHOT"
description = "Insights and hands-on work with PDF generation. Generation based on html with chromium via playwright-java"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-webclient")
    implementation("com.microsoft.playwright:playwright:1.57.0")
    implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")

    implementation("eu.europa.ec.joinup.sd-dss:dss-pades-pdfbox:6.3")
    implementation("eu.europa.ec.joinup.sd-dss:dss-token:6.3")
    implementation("eu.europa.ec.joinup.sd-dss:dss-service:6.3")
    implementation("eu.europa.ec.joinup.sd-dss:dss-utils-apache-commons:6.3")
    implementation("eu.europa.ec.joinup.sd-dss:dss-cms-object:6.3")


    implementation("io.github.openhtmltopdf:openhtmltopdf-pdfbox:1.1.36")
    constraints { implementation("org.apache.pdfbox:pdfbox:3.0.6") }



	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaExec> {
    environment("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1")
}

tasks.withType<Test> {
	useJUnitPlatform();
    environment("PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD", "1");
}

// for local dev:
// 1. open run config
// 2. add before launch gradle task: installPlaywrightChromium
tasks.named("bootRun") {
    dependsOn("installPlaywrightChromium")
}

tasks.named("build") {
    dependsOn("installPlaywrightChromium")
}

tasks.named("test") {
    dependsOn("installPlaywrightChromium")
}

// ./gradlew playwrightInstall
tasks.register<JavaExec>("installPlaywrightChromium") {
    group = "playwright"
    description = "Install Playwright Chromium browser"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.microsoft.playwright.CLI")
    args("install", "--with-deps", "--no-shell", "chromium")

    // environment("PLAYWRIGHT_DOWNLOAD_HOST", "https://my-corp-proxy.com/playwright")
}

// ./gradlew cleanPlaywrightBrowsers
tasks.register<JavaExec>("uninstallPlaywrightChromium") {
    group = "playwright"
    description = "Uninstall Playwright Chromium browser"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.microsoft.playwright.CLI")
    args("uninstall", "--all")
}

// ./gradlew reinstallPlaywrightChromium
tasks.register("reinstallPlaywrightChromium") {
    group = "playwright"
    dependsOn("cleanPlaywrightBrowsers", "installPlaywrightChromium")
}