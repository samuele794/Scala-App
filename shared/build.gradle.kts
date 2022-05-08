import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.*

val ktorfitVersion = "1.0.0-beta04"

val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    // https://github.com/LouisCAD/CompleteKotlin
    id("com.louiscad.complete-kotlin") version "1.1.0"
    kotlin("plugin.serialization") version "1.6.10"
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.codingfeline.buildkonfig")
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
    id("kotlin-parcelize")
}

version = "1.0"

kotlin {
    android()
    ios()
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
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

                api("co.touchlab:kermit:1.1.1")
                api("co.touchlab:kermit-crashlytics:1.1.1")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

                // https://github.com/GitLiveApp/firebase-kotlin-sdk
                implementation("dev.gitlive:firebase-firestore:1.6.1")
                implementation("dev.gitlive:firebase-common:1.6.1")
                api("dev.gitlive:firebase-auth:1.6.1")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

                api("dev.icerock.moko:resources:0.19.0")
                api("dev.icerock.moko:parcelize:0.8.0")

                implementation("de.jensklingenberg.ktorfit:ktorfit-lib:$ktorfitVersion")

                implementation("io.ktor:ktor-client-core:2.0.1")
                implementation("io.ktor:ktor-client-cio:2.0.1")
                implementation("io.ktor:ktor-client-logging:2.0.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")


            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

//                implementation("dev.icerock.moko:resources-test:0.18.0")
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

                api("dev.icerock.moko:resources-compose:0.19.0")

                api("com.google.android.gms:play-services-maps:18.0.2")

            }
        }
        val androidTest by getting
        val iosMain by getting {

        }
        val iosTest by getting {

        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }

    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 31
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "it.samuele794.scala.resources"
    multiplatformResourcesClassName = "SharedRes"
}

buildkonfig {
    packageName = "it.samuele794.scala"

    defaultConfigs {
        buildConfigField(STRING, "GOOGLE_API_BASE_URL", "https://maps.googleapis.com/")
    }
}

dependencies {
    add("kspMetadata", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspAndroid", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosX64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspIosSimulatorArm64", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
}