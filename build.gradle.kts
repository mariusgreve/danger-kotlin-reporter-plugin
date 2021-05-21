plugins {
    id("maven-publish")
    kotlin("jvm") version "1.4.10"
}

val githubUsername: String by project
val githubPassword: String by project

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("systems.danger:danger-kotlin-sdk:1.1")
    testImplementation("junit:junit:4.12")
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/mariusgreve/danger-kotlin-reporter-plugin/")
            credentials {
                username = githubUsername
                password = githubPassword
            }
        }
    }
    publications {
        create<MavenPublication>("default") {
            groupId = "com.mariusgreve.danger"
            artifactId = "danger-kotlin-reporter-plugin"
            version = "0.0.1"

            from(components["java"])
        }
    }
}
