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
    implementation("com.microsoft.playwright:playwright:1.57.0")
    implementation("org.thymeleaf:thymeleaf:3.1.3.RELEASE")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
