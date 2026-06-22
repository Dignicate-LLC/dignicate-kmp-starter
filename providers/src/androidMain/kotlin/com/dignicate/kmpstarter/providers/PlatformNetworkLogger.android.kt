package com.dignicate.kmpstarter.providers

import android.util.Log

internal actual fun platformLogNetwork(message: String) {
    Log.d(NETWORK_LOG_TAG, message)
}