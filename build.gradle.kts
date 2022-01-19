import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.kotlin.kapt") version "1.4.10"
}

application {
    mainClassName = "com.raycoarana.poitodiscover.PoiToDiscoverMediaKt"
}

group = "com.raycoarana"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")

    //Logs
    implementation("org.slf4j:slf4j-api:1.8.0-beta2")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta2")

    //Dagger
    implementation("com.google.dagger:dagger:2.17")
    kapt("com.google.dagger:dagger-compiler:2.17")

    //CSV
    implementation("com.opencsv:opencsv:4.0")

    //SQLite
    implementation("org.xerial:sqlite-jdbc:3.23.1")

    testImplementation("junit:junit:4.12")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }

    from(configurations.runtime.get().files().map {if (it.isDirectory) it else zipTree(it)})
}