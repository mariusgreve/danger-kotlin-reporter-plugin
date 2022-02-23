plugins {
    id("maven-publish")
    id("signing")
    kotlin("jvm") version "1.4.10"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

val githubUsername: String by project
val githubPassword: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("systems.danger:danger-kotlin-sdk:1.2")
    testImplementation("junit:junit:4.12")
}

group = "com.mariusgreve"
version = "0.0.1"

java {
    withSourcesJar()
    withJavadocJar()
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/mariusgreve/danger-kotlin-reporter-plugin/")
            credentials {
                username = githubUsername
                password = githubPassword
            }
        }
    }

    publications {
        create<MavenPublication>("default") {
            groupId = "com.mariusgreve"
            artifactId = "danger-kotlin-reporter-plugin"
            version = "0.0.1"

            from(components["java"])

            pom {
                name.set("Danger Kotlin - Reporter Plugin")
                description.set("A plugin for Danger Kotlin to Report checkstyle like reports")
                url.set("https://github.com/mariusgreve/danger-kotlin-reporter-plugin")
                developers {
                    developer {
                        id.set("mariusgreve")
                        name.set("Marius Greve Hagen")
                        email.set("marius.greve.hagen@gmail.com")
                    }
                }
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://github.com/mariusgreve/danger-kotlin-reporter-plugin/blob/main/LICENSE")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/mariusgreve/danger-kotlin-reporter-plugin.git")
                    developerConnection.set("scm:git:ssh://github.com/mariusgreve/danger-kotlin-reporter-plugin.git")
                    url.set("https://github.com/mariusgreve/danger-kotlin-reporter-plugin")
                }
            }
        }
    }
}
