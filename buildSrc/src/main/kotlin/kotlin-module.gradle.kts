// https://docs.gradle.org/current/userguide/custom_plugins.html#sec:precompiled_plugins
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    // use jupiter engine https://junit.org/junit5/docs/current/user-guide/#running-tests-build-gradle-engines-configure
    testImplementation("org.junit.jupiter:junit-jupiter:[5.9, 6.0)")
    testImplementation("io.mockk:mockk:[1.13,2.0)")
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        allWarningsAsErrors = true
    }
}

tasks.withType<Test> {
    // use JUnit5
    useJUnitPlatform()
    // enable logging exceptions
    testLogging {
        events(TestLogEvent.FAILED, TestLogEvent.SKIPPED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}
