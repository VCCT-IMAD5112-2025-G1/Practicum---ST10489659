plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)

}

android {
    // Your existing android configuration
    namespace = "za.co.varsitycollege.st10489659_practicum" // Make sure you have a namespace
    compileSdk = 35 // Or your desired compileSdk

    defaultConfig {
        applicationId = "za.co.varsitycollege.st10489659_practicum"
        minSdk = 24 // Or your desired minSdk
        targetSdk = 34 // Or your desired targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Your existing dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    // ... other dependencies
}