plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "prog7314.poe.edubridge"
    compileSdk = 37

    defaultConfig {
        applicationId = "prog7314.poe.edubridge"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        //-----REQUIRED CONFIGURATIONS FOR API + WEATHER + MAPS-----//
        buildConfigField("String", "API_BASE_URL", "\"https://dev-api.edubridge.local/\"")
        buildConfigField("String", "WEATHER_API_KEY", "\"YOUR_OPENWEATHER_KEY\"")
        buildConfigField("String", "MAPS_API_KEY", "\"YOUR_MAPS_KEY\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"https://dev-api.edubridge.local/\"")

        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "API_BASE_URL", "\"https://api.edubridge.co.za/\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
    }
}

kotlin {
    jvmToolchain(21)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.generateKotlin", "true")
}

dependencies {
    // Member 1 — AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

    // Kotlin dependencies - KHUMO MACHOGA //
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.gson)

    //Member 2 — API tests
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.truth)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    //--- ROOM DATABASE DEPENDENCIES - ARLO STAPLES ---//
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
    //--- DATASTORE + SECURITY + COROUTINES DEPENDENCIES - ARLO STAPLES ---//
    implementation(libs.datastore.preferences)
    implementation(libs.security.crypto)
    implementation(libs.coroutines.android)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

// Hilt WorkManager
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)
    implementation(libs.work.runtime.ktx)

// Coroutines
    implementation(libs.coroutines.android)

    //--- WORK MANAGER DEPENDENCIES - ARLO STAPLES
    // Enables java.time.* (Instant, LocalDate, etc.) on API < 26
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")

}