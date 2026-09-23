plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.splitwise"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.splitwise"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
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
        viewBinding= true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Material
    implementation(libs.material)


    // =========================
    // Lifecycle / ViewModel
    // =========================

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)


    // =========================
    // Coroutines + Flow
    // =========================

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)


    // =========================
    // Navigation
    // =========================

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)


    // =========================
    // Room
    // =========================

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    ksp(libs.androidx.room.compiler)


    // =========================
    // Retrofit
    // =========================

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // OkHttp logging
    implementation(libs.okhttp.logging.interceptor)


    // =========================
    // Hilt
    // =========================

    implementation(libs.hilt.android)

    ksp(libs.hilt.compiler)


    // =========================
    // Tests
    // =========================

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}