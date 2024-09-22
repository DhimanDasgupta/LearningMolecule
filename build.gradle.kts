// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.ksp) apply false
    // kotlin("jvm") version libs.versions.kotlin // or kotlin("multiplatform") or any other kotlin plugin
    // kotlin("plugin.serialization") version libs.versions.kotlin
}