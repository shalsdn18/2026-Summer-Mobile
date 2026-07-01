# JulyFirstApplication — Notes (2026-07-01)

Date: 2026-07-01

Summary
- Enabled ViewBinding for the viewbindingex module to resolve ActivityMainBinding not being generated.
- File changed: viewbindingex/build.gradle.kts — added:

    buildFeatures {
        viewBinding = true
    }

- Layout checked: viewbindingex/src/main/res/layout/activity_main.xml exists.
- Code using binding: viewbindingex/src/main/java/kr/hnu/ice/viewbindingex/MainActivity.kt (uses ActivityMainBinding.inflate(layoutInflater)).

How to verify
1. Open the project in Android Studio and run Gradle Sync.
2. Or from terminal at project root:

   Windows: gradlew :viewbindingex:assembleDebug
   (or) gradlew assembleDebug

3. If binding classes are still missing: run `gradlew clean assembleDebug`, then rebuild in Android Studio. If problems persist: Invalidate Caches & Restart.

Commit
- Commit created: "Enable viewBinding for viewbindingex module" (includes the modified build.gradle.kts).

Notes / Troubleshooting
- Ensure module namespace matches package used in code (kr.hnu.ice.viewbindingex) and layout file name is activity_main.xml — viewBinding generates ActivityMainBinding from that layout name.
- If multiple modules use view binding, enable it per-module in each module's build.gradle.kts.

If you want, add more daily notes or format this README differently.