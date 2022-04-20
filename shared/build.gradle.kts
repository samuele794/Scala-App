plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    // https://github.com/LouisCAD/CompleteKotlin
    id("com.louiscad.complete-kotlin") version "1.1.0"
    kotlin("plugin.serialization") version "1.6.10"
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }

        //pod("GoogleSignIn")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.insert-koin:koin-core:3.1.6")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

                api("co.touchlab:kermit:1.1.0")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

                // https://github.com/GitLiveApp/firebase-kotlin-sdk
                implementation("dev.gitlive:firebase-firestore:1.5.0")
                implementation("dev.gitlive:firebase-common:1.5.0")
                api("dev.gitlive:firebase-auth:1.5.0")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

                implementation(project.dependencies.platform("com.google.firebase:firebase-bom:29.3.1"))

                // Declare the dependency for the Firebase Authentication library
                // When using the BoM, you don't specify versions in Firebase library dependencies
                implementation("com.google.firebase:firebase-auth-ktx")

                // Also declare the dependency for the Google Play services library and specify its version
                implementation("com.google.android.gms:play-services-auth:20.1.0")

                // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-play-services
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.1")

            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 32
    }
}