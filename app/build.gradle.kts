plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.application_shopii"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.application_shopii"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("androidx.appcompat:appcompat:1.4.1")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")


    //firestore
    implementation("com.google.firebase:firebase-firestore")
    //authentication
    implementation("com.google.firebase:firebase-auth")
    //storage
    implementation("com.google.firebase:firebase-storage")
    //glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //reponsive size
    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    //reponsive size
    implementation ("com.intuit.ssp:ssp-android:1.1.1")
}