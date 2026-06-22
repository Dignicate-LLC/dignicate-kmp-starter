package com.dignicate.kmpstarter.providers

import io.ktor.client.plugins.logging.Logger

internal const val NETWORK_LOG_TAG = "KmpStarterHttp"

internal val platformNetworkLogger: Logger = object : Logger {
    override fun log(message: String) {
        platformLogNetwork(message)
    }
}

internal expect fun platformLogNetwork(message: String)