import es.architectcoders.buildSrc.Libs

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(mapOf("path" to ":domain")))

    // Coroutines
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(project(":domain"))
    implementation(project(":testShared"))
    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    // Javax inject
    implementation(Libs.JavaX.inject)
    // Arrow kt
    implementation(Libs.Arrow.core)
    implementation("io.arrow-kt:arrow-core:1.1.5")
    // Mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    //Test
    testImplementation(Libs.JUnit.junit)
}