plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.gms)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.firebase.crashlytics)


    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)

    //alias(libs.plugins.androidx.room)



}
/*
room {
    // Путь относительно корневого каталога проекта
    schemaDirectory("$projectDir/schemas")
}

 */

android {
    namespace = "best.mobile.stenik"
    compileSdk = 36

    defaultConfig {
        applicationId = "best.mobile.stenik"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }


}

dependencies {
    // подключение модулей
    implementation(project(":entities"))
    implementation(project(":domain"))
    implementation(project(":data"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.ads.mobile.sdk)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // подключение компонента навигации
    ///Navigation Dependency
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material)

    //подключение koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)


    //FireBase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    //JSON
    implementation(libs.kotlinx.serialization.json)

    //чтобы проверить разрешения. Использую в экране редактирования слов. Для микрофона.
    implementation(libs.accompanist.permissions)


    // Room components
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // Поддержка Kotlin Coroutines для Room
    implementation(libs.androidx.room.ktx)


}