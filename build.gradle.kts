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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // swagger
    implementation(
        "org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0",
    ) // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    // swagger

    // QueryDSL
    implementation("com.querydsl:querydsl-apt:5.1.0") // https://mvnrepository.com/artifact/com.querydsl/querydsl-apt
    kapt("com.querydsl:querydsl-apt:5.1.0")
    // QueryDSL

    runtimeOnly("com.mysql:mysql-connector-j") // for main
    testImplementation("com.h2database:h2")    // for test

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // kotest (intellij plugin kotest needed)
    testImplementation(
        "io.kotest:kotest-runner-junit5:5.9.1",
    ) // https://mvnrepository.com/artifact/io.kotest/kotest-runner-junit5
    testImplementation(
        "io.kotest:kotest-assertions-core:5.9.1",
    ) // https://mvnrepository.com/artifact/io.kotest/kotest-assertions-core
    testImplementation(
        "io.kotest:kotest-property:5.9.1",
    ) // https://mvnrepository.com/artifact/io.kotest/kotest-property
    testImplementation(
        "io.kotest.extensions:kotest-extensions-spring:1.3.0",
    ) // https://mvnrepository.com/artifact/io.kotest.extensions/kotest-extensions-spring
    // kotest

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
