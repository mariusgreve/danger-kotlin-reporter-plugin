plugins {
    id("maven-publish")
    id("signing")
    kotlin("jvm") version "1.4.10"
}

val githubUsername: String by project
val githubPassword: String by project
val sonatypeUsername: String by project
val sonatypePassword: String by project

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("systems.danger:danger-kotlin-sdk:1.1")
    testImplementation("junit:junit:4.12")
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

publishing {
    repositories {
        maven {
            name = "Sonatype"
            val releasesRepoUrl =
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
            val snapshotsRepoUrl =
                uri("https://oss.sonatype.org/content/repositories/snapshots")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
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
                scm {
                    connection.set("scm:git:git://github.com/mariusgreve/danger-kotlin-reporter-plugin.git")
                    developerConnection.set("scm:git:ssh://github.com/mariusgreve/danger-kotlin-reporter-plugin.git")
                    url.set("https://github.com/mariusgreve/danger-kotlin-reporter-plugin")
                }
            }
        }
    }
}
