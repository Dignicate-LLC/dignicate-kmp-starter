package com.dignicate.kmpstarter.core

import dignicate_kmp_starter.core.generated.resources.Res
import kotlinx.coroutines.runBlocking

data class RuntimeConfig(
    val env: String,
    val apiBaseUrl: String,
)

object RuntimeConfigLoader {
    fun fromYaml(yaml: String): RuntimeConfig {
        val values = yaml
            .lineSequence()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .mapNotNull { line ->
                val index = line.indexOf(':')
                if (index <= 0) {
                    null
                } else {
                    val key = line.substring(0, index).trim()
                    val value = line.substring(index + 1).trim().trim('"', '\'')
                    key to value
                }
            }
            .toMap()

        val env = values["env"]
            ?: throw IllegalArgumentException("env is required in runtime config")
        val apiBaseUrl = values["apiBaseUrl"]
            ?: throw IllegalArgumentException("apiBaseUrl is required in runtime config")

        return RuntimeConfig(
            env = env,
            apiBaseUrl = apiBaseUrl,
        )
    }
}

fun loadRuntimeConfigYaml(): String? = runBlocking {
    runCatching {
        val bytes = Res.readBytes("files/config.yaml")
        bytes.decodeToString()
    }.getOrNull()
}

fun loadRuntimeConfig(): RuntimeConfig {
    val yaml = loadRuntimeConfigYaml()
        ?: error("files/config.yaml not found in Compose Resources. Check build-time generation task.")
    return RuntimeConfigLoader.fromYaml(yaml)
}
