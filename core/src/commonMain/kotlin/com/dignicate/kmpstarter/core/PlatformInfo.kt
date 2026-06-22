package com.dignicate.kmpstarter.core

data class PlatformInfo(
    val version: String,
    val envName: String,
    val packageName: String,
    val isDebug: Boolean,
)

fun buildAppConfig(platformInfo: PlatformInfo): AppConfig =
    AppConfig(
        version = platformInfo.version,
        env = AppEnvironment.fromName(platformInfo.envName),
        packageName = platformInfo.packageName,
        isDebug = platformInfo.isDebug,
        runtimeConfig = loadRuntimeConfig(),
    )
