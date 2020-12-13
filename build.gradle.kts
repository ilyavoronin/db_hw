plugins {
    java
    id("application")
    kotlin("jvm") version "1.4.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.postgresql:postgresql:42.2.18")
    testCompile("junit", "junit", "4.12")
}

application {
    mainClassName = "MainKt"
}
