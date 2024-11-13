import es.architectcoders.buildSrc.Libs

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
    //secrets-gradle-plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    //kotlin kpt plugin
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "es.architectcoders.spaceexplorer"
    compileSdk = 34

    defaultConfig {
        applicationId = "es.architectcoders.spaceexplorer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        //Con esto ya se ejecutara con el runner de hilt y ya podremos ejecutar test que esten
        // anotados con la anotacion correspondiente de hilt
        testInstrumentationRunner = "es.architectcoders.spaceexplorer.di.HiltTestRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

        buildFeatures {
            buildConfig = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
        viewBinding = true
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":usecases"))
    implementation(project(":data"))
    implementation(project(":testShared"))

    //Core
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.recyclerView)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.constraintLayout)
    implementation(Libs.AndroidX.legacySupport)

    // Activity
    implementation(Libs.AndroidX.Activity.ktx)
    // Fragment
    implementation(Libs.AndroidX.Fragment.ktx)
    // Lifecycle
    implementation(Libs.AndroidX.Lifecycle.runtimeKtx)
    implementation(Libs.AndroidX.Lifecycle.viewmodelKtx)
    // Navigation
    implementation(Libs.AndroidX.Navigation.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.uiKtx)
    //splash
    implementation(Libs.AndroidX.SplashScreen.ktx)
    // Room
    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.ktx)
    annotationProcessor(Libs.AndroidX.Room.compiler)
    kapt(Libs.AndroidX.Room.compiler)
    // Coroutines
    implementation(Libs.Kotlin.Coroutines.core)
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.activity:activity-ktx:1.8.2")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    // Hilt
    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.dagger:hilt-android-testing:2.49")
    kapt("com.google.dagger:hilt-compiler:2.49")
    // Livedata
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    // Retrofit
    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.converterGson)
    // OkHttp
    implementation(Libs.OkHttp3.loginInterceptor)
    // Maps
    implementation(Libs.PlayServices.maps)
    // Arrow kt
    implementation(Libs.Arrow.core)
    //Glide
    implementation (Libs.Glide.glide)
    kapt (Libs.Glide.compiler)
    // Shimmer
    implementation(Libs.Shimmer.shimmer)
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    // Mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.mockito:mockito-inline:4.8.0")
    //Turbine
    testImplementation("app.cash.turbine:turbine:0.12.1")
    //Runner
    implementation ("androidx.test:runner:1.5.2")
    //Rules
    implementation ("androidx.test:rules:1.5.0")
    //Test
    testImplementation(Libs.JUnit.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.Espresso.contrib)
    androidTestImplementation(Libs.AndroidX.Test.runner)
    androidTestImplementation(Libs.AndroidX.Test.rules)
    androidTestImplementation(Libs.Hilt.test)
    androidTestImplementation(Libs.Kotlin.Coroutines.test)
    androidTestImplementation(Libs.OkHttp3.mockWebServer)
    androidTestImplementation ("androidx.navigation:navigation-testing:2.8.3")

    debugImplementation("androidx.fragment:fragment-testing-manifest:1.8.5")

    androidTestImplementation("androidx.fragment:fragment-testing:1.8.5")

    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.49")
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:2.49")

    kaptAndroidTest(Libs.Hilt.compiler)

    testImplementation("junit:junit:4.13.2")
    testImplementation(project(":testShared"))
    testImplementation(project(":appTestShared"))
    testImplementation(project(":data"))
    testImplementation(project(":usecases"))
//    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation(project(":appTestShared"))

}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}