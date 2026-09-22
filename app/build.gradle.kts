import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "prog7314.poe.edubridge"
    compileSdk = 35

    defaultConfig {
        applicationId = "prog7314.poe.edubridge"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["MAPS_API_KEY"] = secret("MAPS_API_KEY")

        buildConfigField("String", "API_BASE_URL", "\"http://127.0.0.1:8080/\"")
        buildConfigField("String", "WEATHER_API_KEY", "\"${secret("WEATHER_API_KEY")}\"")
        buildConfigField("String", "MAPS_API_KEY", "\"${secret("MAPS_API_KEY")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/io.netty.versions.properties"
        }
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
    // ── AndroidX ──────────────────────────────────────
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

    // ── Compose ───────────────────────────────────────
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // ── Hilt Navigation Compose (needed for hiltViewModel()) ──
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // ── Ktor (Member 2 — Khumo Machoga) ───────────────
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.gson)

    // ── Tests (Member 2 — API tests — Khumo Machoga) ──
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.truth)
    testImplementation(libs.coroutines.test)

    // ── Room (Arlo Staples) ───────────────────────────
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // ── DataStore + Security + Coroutines (Arlo Staples) ──
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.security.crypto)
    implementation(libs.coroutines.android)

    // ── Retrofit ──────────────────────────────────────
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // ── Hilt ──────────────────────────────────────────
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // ── Hilt WorkManager ──────────────────────────────
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)
    implementation(libs.work.runtime.ktx)

    // ── Desugaring (java.time.* on API < 26) ──────────
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // ── Google Maps (Member 3) ────────────────────────
    implementation("com.google.maps.android:maps-compose:4.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
}

fun secret(key: String, fallback: String = ""): String {
    val file = rootProject.file("local.properties")
    if (!file.exists()) {
        return System.getenv(key) ?: fallback
    }
    val props = Properties()
    file.inputStream().use { props.load(it) }
    return props.getProperty(key) ?: System.getenv(key) ?: fallback
}