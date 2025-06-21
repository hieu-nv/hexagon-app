plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.0" apply true
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.hieunv"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}



subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")

    group = "com.hieunv"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.0")
        }
        dependencies {
            dependency("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
            dependency("io.jsonwebtoken:jjwt-api:0.11.5")
            dependency("io.jsonwebtoken:jjwt-impl:0.11.5")
            dependency("io.jsonwebtoken:jjwt-jackson:0.11.5")
            dependency("org.hibernate.orm:hibernate-community-dialects:7.0.0.Final")
            dependency("org.xerial:sqlite-jdbc:3.50.1.0")
        }
    }
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

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    mainClass.set("com.hieunv.app.App")
}
