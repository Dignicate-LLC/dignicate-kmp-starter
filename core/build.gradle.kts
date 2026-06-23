plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
}

val appEnv = providers.gradleProperty("appEnv").orElse("dev")
val allowedEnvs = setOf("dev", "stg", "prod")

val selectedEnv = appEnv.get().also {
    require(it in allowedEnvs) {
        "Invalid -PappEnv=$it. Expected one of: dev|stg|prod"
    }
}

val sourceRuntimeConfig = layout.projectDirectory.file("../config/$selectedEnv/config.yaml")
val generatedComposeResourcesDir = layout.buildDirectory.dir("generated/runtimeConfig/composeResources")

val generateRuntimeConfigForComposeResources = tasks.register<org.gradle.api.tasks.Copy>("generateRuntimeConfigForComposeResources") {
    from(sourceRuntimeConfig)
    into(generatedComposeResourcesDir.map { it.dir("files") })
    rename { "config.yaml" }
}

compose.resources {
    customDirectory(
        sourceSetName = "commonMain",
        directoryProvider = generateRuntimeConfigForComposeResources.map { generatedComposeResourcesDir.get() }
    )
}

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
            baseName = "Core"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.components.resources)
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
    namespace = "com.dignicate.kmpstarter.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
