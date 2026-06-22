package com.dignicate.kmpstarter.core

actual fun loadRuntimeConfigYaml(): String? {
    val classLoader = RuntimeConfig::class.java.classLoader ?: return null
    return classLoader
        .getResourceAsStream("assets/runtime/config.yaml")
        ?.bufferedReader()
        ?.use { it.readText() }
}
