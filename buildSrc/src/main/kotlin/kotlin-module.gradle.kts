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
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
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
