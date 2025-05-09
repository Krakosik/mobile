plugins {
    id(
        libs.plugins.org.jetbrains.kotlin.android
            .get()
            .pluginId,
    )
    kotlin("kapt")
    alias(libs.plugins.com.google.dagger.hilt.android)
    id(
        libs.plugins.com.android.library
            .get()
            .pluginId,
    )
    id(
        libs.plugins.com.google.protobuf
            .get()
            .pluginId,
    )
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
}

configureProtobuf()

android {
    namespace = "com.pawlowski.network"
    compileSdk = ProjectConfig.compileSdk

    sourceSets.getByName("main") {
        setProtoPath(srcPath = "src/main/proto")
        java.srcDirs(
            "build/generated/source/proto/main/grpc",
            "build/generated/source/proto/main/grpckt",
            "build/generated/source/proto/main/java",
        )
    }

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.bundles.grpc)
    implementation(platform(libs.org.jetbrains.kotlinx.kotlinx.serialization.bom))
    implementation(libs.bundles.serialization)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
}
