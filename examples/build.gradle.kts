plugins {
    java
    kotlin("jvm") version "2.2.0"
    id("com.diffplug.spotless") version "8.4.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

// Keep in sync with hugo.yaml params
val tabletestVersion = "1.2.1"
val junitVersion = "6.0.3"

dependencies {
    testImplementation("org.tabletest:tabletest-junit:$tabletestVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
    java {
        target("src/**/*.java")
        tableTestFormatter()
    }
    kotlin {
        target("src/**/*.kt")
        tableTestFormatter()
    }
    tableTest {
        target("src/**/*.table")
        tableTestFormatter()
    }
}

tasks.test {
    useJUnitPlatform()
}
