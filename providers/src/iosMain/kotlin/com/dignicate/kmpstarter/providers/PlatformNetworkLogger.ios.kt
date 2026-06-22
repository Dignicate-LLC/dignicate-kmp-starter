package com.dignicate.kmpstarter.providers

import platform.Foundation.NSLog

internal actual fun platformLogNetwork(message: String) {
    NSLog("%@: %@", NETWORK_LOG_TAG, message)
}