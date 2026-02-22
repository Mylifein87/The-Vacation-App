plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
}



android {
    namespace = "com.example.d308project"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.d308project"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.common)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.room:room-gradle-plugin:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4") // optional - RxJava2 support for Room implementation("androidx.room:room-rxjava2:$room_version") // optional - RxJava3 support for Room implementation("androidx.room:room-rxjava3:$room_version") // optional - Guava support for Room, including Optional and ListenableFuture implementation("androidx.room:room-guava:$room_version") // optional - Test helpers testImplementation("androidx.room:room-testing:$room_version") // optional - Paging 3 Integration implementation("androidx.room:room-paging:$room_version")




}



