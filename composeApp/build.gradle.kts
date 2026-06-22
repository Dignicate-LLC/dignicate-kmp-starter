plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

val runtimeConfigRoot = rootProject.layout.projectDirectory.dir("config")

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(project(":core"))
            implementation(project(":ui"))
            implementation(project(":providers"))
            implementation(project(":viewmodel"))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.dignicate.kmpstarter"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.dignicate.kmpstarter"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = libs.versions.app.version.get()
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "APP_ENV", "\"dev\"")
        }
        create("stg") {
            dimension = "environment"
            applicationIdSuffix = ".stg"
            versionNameSuffix = "-stg"
            buildConfigField("String", "APP_ENV", "\"stg\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "APP_ENV", "\"prod\"")
        }
    }

    sourceSets["dev"].assets.srcDir(layout.buildDirectory.dir("generated/runtimeConfig/dev"))
    sourceSets["stg"].assets.srcDir(layout.buildDirectory.dir("generated/runtimeConfig/stg"))
    sourceSets["prod"].assets.srcDir(layout.buildDirectory.dir("generated/runtimeConfig/prod"))

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

val copyRuntimeConfigDev = tasks.register<org.gradle.api.tasks.Copy>("copyRuntimeConfigDev") {
    from(runtimeConfigRoot.file("dev/config.yaml"))
    into(layout.buildDirectory.dir("generated/runtimeConfig/dev/runtime"))
    rename { "config.yaml" }
}

val copyRuntimeConfigStg = tasks.register<org.gradle.api.tasks.Copy>("copyRuntimeConfigStg") {
    from(runtimeConfigRoot.file("stg/config.yaml"))
    into(layout.buildDirectory.dir("generated/runtimeConfig/stg/runtime"))
    rename { "config.yaml" }
}

val copyRuntimeConfigProd = tasks.register<org.gradle.api.tasks.Copy>("copyRuntimeConfigProd") {
    from(runtimeConfigRoot.file("prod/config.yaml"))
    into(layout.buildDirectory.dir("generated/runtimeConfig/prod/runtime"))
    rename { "config.yaml" }
}

tasks.configureEach {
    when {
        name.startsWith("preDev") && name.endsWith("Build") -> dependsOn(copyRuntimeConfigDev)
        name.startsWith("preStg") && name.endsWith("Build") -> dependsOn(copyRuntimeConfigStg)
        name.startsWith("preProd") && name.endsWith("Build") -> dependsOn(copyRuntimeConfigProd)
    }
}
