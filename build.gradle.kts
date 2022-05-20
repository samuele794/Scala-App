buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("dev.icerock.moko:resources-generator:0.19.0")
        classpath("com.google.firebase:firebase-appdistribution-gradle:3.0.1")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://repo1.maven.org/maven2/dev/gitlive/")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}