plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_20
    targetCompatibility = JavaVersion.VERSION_20
}

// kotlin {
//    jvmToolchain {
//        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(20)) // Asegúrate de que Kotlin use la JVM 17
//    }
// }

dependencies {
    // Either
    implementation(libs.arrow.core)
}
