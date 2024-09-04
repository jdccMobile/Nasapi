plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":domain"))

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
// Coroutines
    implementation(libs.kotlinx.coroutines)
    // Either
    implementation(libs.arrow.core)
    // Room
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
}
