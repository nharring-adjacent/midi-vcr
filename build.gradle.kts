plugins {
    // Kotlin plugin: same version as modules
    kotlin("jvm") version "1.8.22" apply false
    id("java-library") apply false
}

// JUnit Jupiter version for tests
val junitJupiterVersion = "5.9.2"

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