val kotlinVersion = "1.8.22"
val junitJupiterVersion = "5.9.2"

plugins {
    kotlin("jvm") version kotlinVersion apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}