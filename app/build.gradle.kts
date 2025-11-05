plugins {
    // plugin de aplicacion de android (para construir el .apk)
    alias(libs.plugins.android.application)
    // plugin de kotlin para android
    alias(libs.plugins.kotlin.android)
    // plugin especifico para jetpack compose
    alias(libs.plugins.kotlin.compose)
}

android {
    // el 'namespace' es el id unico de tu app en el sistema android
    namespace = "com.example.levelupgamermobile"
    // la version del sdk de android con la que se compila la app
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.levelupgamermobile"
        // la version minima de android que soporta la app
        minSdk = 33
        // la version de android para la que fue disenada
        targetSdk = 36
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
    // configuracion para usar java 11
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    // habilita la funcionalidad de jetpack compose
    buildFeatures {
        compose = true
    }
}

// definicion de todas las librerias que usa la app
dependencies {
    // --- core y activity
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // --- jetpack compose (bom, ui, material)
    // la 'bill of materials' (bom) asegura que todas las
    // librerias de compose usen versiones compatibles entre si.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- lifecycle (viewmodel)
    // (anadidas manualmente)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // --- navegacion
    implementation(libs.androidx.navigation.compose)
    // libreria para los iconos extendidos de material
    implementation(libs.androidx.compose.material.icons)

    // --- persistencia (datastore)
    // para guardar la sesion
    implementation(libs.androidx.datastore.preferences)

    // --- red (backend)
    // retrofit para la api, okhttp para logging, gson para conversion json
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

    // --- pantalla de carga (splash screen)
    // (dependencias anadidas manualmente y desde libs)
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.navigation:navigation-compose:2.9.5")
    implementation("androidx.compose.animation:animation-core:...") // (dependencia de animacion incompleta)
    implementation(libs.androidx.core.splashscreen)

    // --- testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}