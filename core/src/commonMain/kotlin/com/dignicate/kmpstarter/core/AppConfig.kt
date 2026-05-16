package com.dignicate.kmpstarter.core

data class AppConfig(
    val version: String,
    val env: AppEnvironment,
    val packageName: String,
) {
    val showsDebugMenu: Boolean get() = !env.isProduction

    companion object {
        fun load(): AppConfig = AppConfig(
            version = getAppVersion(),
            env = AppEnvironment.UNKNOWN,
            packageName = getPackageName(),
        )
    }
}
