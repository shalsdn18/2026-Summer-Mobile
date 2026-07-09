plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "kr.hnu.ice.mydrawer"

    // 1. compileSdk 올바른 표기법으로 수정
    compileSdk = 37

    // 2. viewBinding 활성화 블록 규격 수정
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "kr.hnu.ice.mydrawer"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // 3. 코드 축소 및 최적화(난독화) 비활성화 표준 문법 적용
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
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}