plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)

    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)

   // alias(libs.plugins.androidx.room)
    //kotlin("plugin.parcelize") // Добавьте эту строку
   // id("org.jetbrains.kotlin.plugin.serialization")

}

android {
    namespace = "best.mobile.data"
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
   // ksp(libs.androidx.room.compiler.v261)

    // подключение модулей
    implementation(project(":entities"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runtime)
    implementation(libs.digital.ink.common)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //подключение koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)


    //FireBase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    //JSON
    implementation(libs.kotlinx.serialization.json)

    // Room components
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // Поддержка Kotlin Coroutines для Room
    implementation(libs.androidx.room.ktx)


    implementation(libs.play.services.tasks)


    // 2. Обязательный модуль для поддержки форматов XLS и XLSX
    implementation(libs.dataframe.excel)


}