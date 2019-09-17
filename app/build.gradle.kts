plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}


val LUFTHANSA_KEY: String by project
val LUTFHANSA_SECRET: String by project
val GOOGLE_MAP_KEY: String by project

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.michaelbukachi.flightschedules"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "com.michaelbukachi.flightschedules.TestInstrumentationRunner"
        buildConfigField("String", "LUFTHANSA_KEY", LUFTHANSA_KEY)
        buildConfigField("String", "LUTFHANSA_SECRET", LUTFHANSA_SECRET)
        resValue("string", "GOOGLE_MAP_KEY", GOOGLE_MAP_KEY)
    }
    buildTypes {
        getByName("debug") {
            buildConfigField("String", "API_BASE_URL", "\"https://api.lufthansa.com/v1/\"")
        }
        getByName("release") {
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

    androidExtensions {
        isExperimental = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.2.1")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.0.2")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.recyclerview:recyclerview:1.1.0-beta01")
    implementation("com.github.tiper:MaterialSpinner:1.3.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.0.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.0.0")

    // Lifecycle + Viewmodel
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-alpha02")
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-alpha02")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.squareup.okhttp3:okhttp:4.0.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.0.1")

    // DI
    implementation("com.google.dagger:dagger:2.24")
    kapt("com.google.dagger:dagger-compiler:2.24")
    kaptAndroidTest("com.google.dagger:dagger-compiler:2.24")


    // logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Shared preferences
    implementation("com.chibatching.kotpref:kotpref:2.8.0")
    implementation("com.chibatching.kotpref:initializer:2.8.0")

    // date/time
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")

    testImplementation("junit:junit:4.12")
    testImplementation("androidx.arch.core:core-testing:2.0.1")
    testImplementation("androidx.test.ext:junit:1.1.1")
    testImplementation("androidx.test:rules:1.2.0")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1")

    debugImplementation("androidx.fragment:fragment-testing:1.1.0-rc03") {
        exclude(mapOf("group" to "androidx.test", "module" to "core"))
    }

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:rules:1.2.0")
    androidTestImplementation("androidx.test.ext:truth:1.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.0.1")

}
