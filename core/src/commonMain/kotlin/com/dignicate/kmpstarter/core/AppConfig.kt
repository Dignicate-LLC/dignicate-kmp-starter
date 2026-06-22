package com.dignicate.kmpstarter.core

data class AppConfig(
    val version: String,
    val env: AppEnvironment,
    val packageName: String,
    val isDebug: Boolean,
    val runtimeConfig: RuntimeConfig,
) {
    val apiBaseUrl: String get() = runtimeConfig.apiBaseUrl
    val runtimeEnvName: String get() = runtimeConfig.env
    val showsDebugMenu: Boolean get() = !env.isProduction
}
