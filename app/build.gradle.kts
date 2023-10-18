plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.finalbustraking"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.finalbustraking"
        minSdk = 25
        targetSdk = 33
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation ("androidx.appcompat:appcompat:1.6.1") // Use the same version as AppCompat
    implementation ("com.google.android.material:material:1.9.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0") // Update to the latest version
    implementation("com.google.firebase:firebase-auth:22.1.2")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    implementation("com.google.firebase:firebase-database:20.2.2") // Update to the latest version
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2") // Update to the latest version
    implementation("com.google.android.gms:play-services-maps:18.1.0") // Update to the latest version
    implementation("com.mappls.sdk:mappls-android-sdk:8.0.8")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    implementation ("com.google.firebase:firebase-auth:21.0.1") // If you also want to use Firebase Authentication
    implementation ("com.google.firebase:firebase-firestore:24.0.0") // Firestore SDK
//    implementation ("com.google.firebase:firebase-database:21.0.1")
//





    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}