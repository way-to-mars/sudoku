plugins {
    kotlin("jvm") version "1.8.10"
    application
}

group = "com.way2mars.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
    jvmToolchain(11)
}

application {
    mainClass.set("com.way2mars.kotlin.sudoku.Main")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}