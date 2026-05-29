plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)

    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    //kotlin("plugin.parcelize") // Добавьте эту строку
   // id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "best.mobile.domain"
    compileSdk = 36

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    // подлючение модулей
    implementation(project(":entities"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.digital.ink.common)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Для mutableStateOf
    implementation(libs.androidx.runtime)


    //подключение koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)

    //FireBase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)

    //JSON
    implementation(libs.kotlinx.serialization.json)


    // Room components
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Поддержка Kotlin Coroutines для Room
    implementation(libs.androidx.room.ktx)
}