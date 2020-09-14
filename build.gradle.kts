import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    application
    antlr
}

group = "me.dkaznacheev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    antlr("org.antlr:antlr4:4.5")
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-package", "me.dk.exproptimizer", "-visitor")
    outputDirectory = File("$buildDir/generated-src/antlr/main/me/dk/exproptimizer")
}

tasks.withType<KotlinCompile>() {
    dependsOn(tasks.generateGrammarSource)
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<JavaCompile>() {
    dependsOn(tasks.generateGrammarSource)
}

sourceSets.main {
    java.srcDirs("$buildDir/generated-src/antlr/main/")
}

application {
    mainClassName = "MainKt"
}