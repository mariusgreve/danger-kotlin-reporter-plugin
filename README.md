[![Maven Central](https://img.shields.io/maven-central/v/com.mariusgreve/danger-kotlin-reporter-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.mariusgreve%22%20AND%20a:%22danger-kotlin-reporter-plugin%22)
# danger-kotlin-reporter-plugin

Shows checkstyle type issues in your PR with [Danger Kotlin].

Works with [ktlint] and [Detekt].

## Setup

Add the following dependency to your `Dangerfile.df.kts` file
```kotlin
@file:DependsOn("com.mariusgreve:danger-kotlin-reporter-plugin:0.0.1")
```
Register the plugin before the `danger` initialisation function and use like this:
```kotlin
register plugin ReporterPlugin

danger(args) {
    ReporterPlugin.report {
        pattern = "**/build/reports/{ktlint,detekt}/**.xml"
    }
    
    ...
}
```
The pattern accepts any glob syntax.

[Danger Kotlin]:https://github.com/danger/kotlin
[ktlint]:https://github.com/pinterest/ktlint
[Detekt]:https://github.com/detekt/detekt
