plugins {
    // Apply the common convention plugin for shared build configuration between library and application projects.
    id("momento.java-common-conventions")

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
}

val mavenUrl: String by extra
val mavenSnapshotUrl: String by extra
val sonatypeUsername: String? by project
val sonatypePassword: String? by project

configure<PublishingExtension> {
    println("\n\n\nCONFIGURING PUBLISHING EXTENSION; version: '${version}'\n\n\n")
    repositories {
        maven {
            url = if (version.toString().endsWith("SNAPSHOT")) {
                uri(mavenSnapshotUrl)
            } else {
                uri(mavenUrl)
            }
            credentials {
                username = sonatypeUsername
                password = sonatypePassword
            }
        }
    }}

