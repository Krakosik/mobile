// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.com.google.android.libraries.mapsplatform.secrets)
}

buildscript {
    dependencies {
        classpath(libs.com.android.tools.build.gradle)
        classpath(libs.org.jetbrains.kotlin.gradle.plugin)
        classpath(libs.com.google.protobuf.gradle.plugin)
    }
}
