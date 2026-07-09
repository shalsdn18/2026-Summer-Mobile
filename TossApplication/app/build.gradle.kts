plugins {
    alias(libs.plugins.android.application)
}

android {
    // image_588133.png의 물리 패키지 트리 구조와 일치하도록 수정
    namespace = "kr.hnu.ice.tossapplication"

    buildFeatures {
        viewBinding = true
    }
    compileSdk {
        version = release(37) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        applicationId = "kr.hnu.ice.tossapplication"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    
    // 생체 인증 라이브러리 추가 (7/8 마일스톤 필수 컴포넌트)
    implementation("androidx.biometric:biometric:1.1.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}