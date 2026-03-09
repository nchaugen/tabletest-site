import org.tabletest.formatter.spotless.TableTestFormatterStep

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.tabletest:tabletest-formatter-spotless:1.0.0")
    }
}

plugins {
    java
    kotlin("jvm") version "2.2.0"
    id("com.diffplug.spotless") version "8.1.0"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// Keep in sync with hugo.yaml params
val tabletestVersion = "1.1.0"
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
        addStep(TableTestFormatterStep.create())
    }
    kotlin {
        target("src/**/*.kt")
        addStep(TableTestFormatterStep.create())
    }
}

tasks.test {
    useJUnitPlatform()
}
