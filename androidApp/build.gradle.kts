import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
    id("kotlin-parcelize")
    id("com.google.firebase.appdistribution")
}

apply(plugin = "com.google.gms.google-services")
apply(plugin = "com.google.firebase.crashlytics")


val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "it.samuele794.scala.android"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "GOOGLE_AUTH_APIKEY",
            "\"${localProperties["auth.google.apikey"]}\""
        )

        buildConfigField(
            "String",
            "EMAIL_TEST_AN",
            "\"${localProperties["auth.email"]}\""
        )

        buildConfigField(
            "String",
            "PASSWORD_TEST_AN",
            "\"${localProperties["auth.password"]}\""
        )
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            firebaseAppDistribution {
                appId = localProperties.getProperty("firebase.projectID")
                artifactType = "APK"
                testers = localProperties.getProperty("firebase.testers")
            }
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}


buildscript {
    repositories {
        google()
    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.10")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
    }
}

dependencies {
    implementation(project(":shared"))
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")

    implementation("io.insert-koin:koin-android:3.1.6")
    implementation("io.insert-koin:koin-androidx-compose:3.1.6")

    implementation(platform("com.google.firebase:firebase-bom:29.3.1"))

    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Also declare the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.1.0")

    // https://composedestinations.rafaelcosta.xyz/
    implementation("io.github.raamcosta.compose-destinations:core:1.5.1-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.5.1-beta")
}