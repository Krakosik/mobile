plugins {
    id(
        libs.plugins.com.android.application
            .get()
            .pluginId,
    )
    id(
        libs.plugins.org.jetbrains.kotlin.android
            .get()
            .pluginId,
    )
    kotlin("kapt")
    alias(libs.plugins.com.google.dagger.hilt.android)
    id(
        libs.plugins.com.google.protobuf
            .get()
            .pluginId,
    )
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.com.google.android.libraries.mapsplatform.secrets)
}

secrets {
    propertiesFileName = "secrets.properties"
}

android {
    namespace = "com.pawlowski.krakosik2"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = "com.pawlowski.krakosik2"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":network"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.accompanist.permissions)
    implementation(libs.bundles.grpc)
    implementation(libs.com.google.protobuf.protobuf.javalite)

    implementation(libs.bundles.navigation)

    implementation(platform(libs.org.jetbrains.kotlinx.kotlinx.serialization.bom))
    implementation(libs.bundles.serialization)

    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)

    implementation(platform(libs.firebase.bom))

    implementation(libs.maps.compose)
    runtimeOnly("org.jetbrains.kotlin:kotlin-metadata-jvm:2.1.0")
}
