---
title: "Installation"
weight: 2
---

TableTest is available on Maven Central. Add it to your project as a test-scoped dependency alongside JUnit.

## Requirements

Before installing TableTest, ensure your project meets these requirements:

- **Kotlin** or **Java** 21 or above
- **JUnit** 5.11 or above

## Setup

{{< tabs items="Maven,Gradle,Gradle Kotlin DSL" >}}
{{< tab >}}
```xml {filename="pom.xml"}
<dependencies>
    <!-- TableTest -->
    <dependency>
        <groupId>org.tabletest</groupId>
        <artifactId>tabletest-junit</artifactId>
        <version>{{< param currentTableTestVersion >}}</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>{{< param currentJUnitVersion >}}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
{{< /tab >}}
{{< tab >}}
```groovy {filename="build.gradle"}
dependencies {
    testImplementation 'org.tabletest:tabletest-junit:{{< param currentTableTestVersion >}}'
    testImplementation 'org.junit.jupiter:junit-jupiter:{{< param currentJUnitVersion >}}'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

test {
    useJUnitPlatform()
}
```
{{< /tab >}}
{{< tab >}}
```kotlin {filename="build.gradle.kts"}
dependencies {
    testImplementation("org.tabletest:tabletest-junit:{{< param currentTableTestVersion >}}")
    testImplementation("org.junit.jupiter:junit-jupiter:{{< param currentJUnitVersion >}}")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
```
{{< /tab >}}
{{< /tabs >}}

## Framework Compatibility

TableTest works with popular Java frameworks:

- **Spring Boot** 3.4.0 and above
- **Quarkus** 3.21.2 and above

## Verification

Create a simple test to verify TableTest is working:

{{< tabs items="Java,Kotlin" >}}
{{< tab >}}
{{< codefile file="examples/src/test/java/getting_started/InstallationTest.java" id="verification" >}}
{{< /tab >}}
{{< tab >}}
{{< codefile file="examples/src/test/kotlin/getting_started/InstallationKtTest.kt" id="verification" >}}
{{< /tab >}}
{{< /tabs >}}

Run your tests. If you see three passing test executions, TableTest is correctly configured!

## IDE Support

For an enhanced development experience, consider installing the IDE plugin for your editor:

- **IntelliJ IDEA** — [TableTest IntelliJ Plugin](https://plugins.jetbrains.com/plugin/27334-tabletest)
- **VS Code** — [TableTest VS Code Extension](https://marketplace.visualstudio.com/items?itemName=tabletest.tabletest)

Both provide automatic table formatting and syntax highlighting. The IntelliJ plugin additionally offers visual feedback for invalid syntax.

## Next Steps

With TableTest installed, you're ready to [write your first test&nbsp;→](/docs/getting-started/first-test)
