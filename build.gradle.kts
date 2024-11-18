import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "2.0.21"
    kotlin("kapt") version "2.0.21"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "pe.swkim"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
//    implementation("org.springframework.boot:spring-boot-starter-hateoas") // Spring HATEOAS와 Spring Data의 PagedResourcesAssembler를 사용하여 Page를 PagedModel로 변환할 수 있기 때문에 hateoas 의존성을 추가해줘야 한다.
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // swagger
    implementation(
        "org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0",
    ) // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    // swagger

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")
    // QueryDSL

    runtimeOnly("com.mysql:mysql-connector-j") // for main
    testImplementation("com.h2database:h2") // for test

    // kotlin-logging
    // https://mvnrepository.com/artifact/io.github.oshai/kotlin-logging-jvm
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")
    // kotlin\-logging

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // kotest (intellij plugin kotest needed)
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-property:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    // kotest

    // Testcontainers
//    testImplementation("org.testcontainers:testcontainers:1.20.3")
//    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
    // Testcontainers

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// xxxx-plain.jar 생성을 하지 않되록 한다 Case 1.
tasks.withType<Jar> {
    enabled = false
}

tasks.withType<BootJar> {
    enabled = true
}
// xxxx-plain.jar 생성을 하지 않되록 한다 Case 1.

/*
tasks.named<Jar>("jar") {
    // xxxx-plain.jar 생성을 하지 않되록 한다 Case 2.
    enabled = false
}
*/
