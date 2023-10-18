buildscript {
    repositories {
        google()
        // Add MapMyIndia SDK repository here
        maven { url = uri("https://repos.mapmyindia.com/repository/mapmyindia/") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath("com.google.gms:google-services:4.4.0")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}
