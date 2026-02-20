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

```java
import org.tabletest.junit.TableTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerificationTest {
    @TableTest("""
        Input | Expected
        1     | 1
        2     | 2
        3     | 3
        """)
    public void verifyTableTestWorks(int input, int expected) {
        assertEquals(expected, input);
    }
}
```

Run your tests. If you see three passing test executions, TableTest is correctly configured!

## IDE Support

For an enhanced development experience, consider installing the [TableTest IntelliJ Plugin](https://plugins.jetbrains.com/plugin/27334-tabletest), which provides:

- Automatic table formatting
- Syntax highlighting
- Visual feedback for invalid syntax
- Code assistance

Install from the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/27334-tabletest).

## Next Steps

With TableTest installed, you're ready to [write your first test&nbsp;→](/docs/getting-started/first-test)
