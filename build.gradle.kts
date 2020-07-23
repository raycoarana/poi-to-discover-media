import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.50"
    id("org.jetbrains.kotlin.kapt") version "1.3.50"
}

application {
    mainClassName = "com.raycoarana.poitodiscover.PoiToDiscoverMediaKt"
}

group = "com.raycoarana"
version = "1.3"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")

    //Logs
    compile("org.slf4j:slf4j-api:1.8.0-beta2")
    compile("org.slf4j:slf4j-simple:1.8.0-beta2")

    //Dagger
    compile("com.google.dagger:dagger:2.17")
    kapt("com.google.dagger:dagger-compiler:2.17")

    //CSV
    compile("com.opencsv:opencsv:4.0")

    //SQLite
    compile("org.xerial:sqlite-jdbc:3.23.1")

    testCompile("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }

    from(configurations.runtime.files().map {if (it.isDirectory) it else zipTree(it)})
}