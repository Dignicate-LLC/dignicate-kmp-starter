package com.dignicate.kmpstarter.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RuntimeConfigLoaderTest {

    @Test
    fun fromYaml_parsesEnvAndApiBaseUrl() {
        val yaml = """
            env: "dev"
            apiBaseUrl: "https://api.example.com"
        """.trimIndent()

        val config = RuntimeConfigLoader.fromYaml(yaml)

        assertEquals("dev", config.env)
        assertEquals("https://api.example.com", config.apiBaseUrl)
    }

    @Test
    fun fromYaml_throwsWhenEnvMissing() {
        val yaml = """
            apiBaseUrl: "https://api.example.com"
        """.trimIndent()

        assertFailsWith<IllegalArgumentException> {
            RuntimeConfigLoader.fromYaml(yaml)
        }
    }

    @Test
    fun fromYaml_throwsWhenApiBaseUrlMissing() {
        val yaml = """
            env: "dev"
        """.trimIndent()

        assertFailsWith<IllegalArgumentException> {
            RuntimeConfigLoader.fromYaml(yaml)
        }
    }
}
