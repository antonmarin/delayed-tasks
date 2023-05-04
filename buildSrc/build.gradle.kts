plugins {
    `kotlin-dsl`
}
// https://docs.gradle.org/current/userguide/kotlin_dsl.html#sec:kotlin-dsl_plugin
repositories {
    mavenCentral()
}

dependencies {
    // https://docs.gradle.org/current/userguide/custom_plugins.html#applying_external_plugins_in_precompiled_script_plugins
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
}
