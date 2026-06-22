package com.dignicate.kmpstarter

import androidx.compose.ui.window.ComposeUIViewController
import com.dignicate.kmpstarter.core.PlatformInfo
import com.dignicate.kmpstarter.providers.startApplication
import kotlin.native.Platform
import platform.Foundation.NSBundle
import platform.UIKit.UIViewController

class MainViewControllerFactory {
    fun make(): UIViewController {
        val bundle = NSBundle.mainBundle
        val info = bundle.infoDictionary
        val version = info?.get("CFBundleShortVersionString") as? String ?: "0.0.0"
        val envName = info?.get("APP_ENV") as? String ?: "UNKNOWN"
        startApplication(
            PlatformInfo(
                version = version,
                envName = envName,
                packageName = bundle.bundleIdentifier ?: "unknown",
                isDebug = Platform.isDebugBinary,
            ),
        )
        return ComposeUIViewController {
            App()
        }
    }
}
