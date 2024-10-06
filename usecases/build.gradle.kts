import es.architectcoders.buildSrc.Libs

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":testShared"))

    // Coroutines
    implementation(Libs.Kotlin.Coroutines.core)
    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    // Javax inject
    implementation(Libs.JavaX.inject)
    implementation("javax.inject:javax.inject:1")
    // Mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    //Test
    testImplementation(Libs.JUnit.junit)
}