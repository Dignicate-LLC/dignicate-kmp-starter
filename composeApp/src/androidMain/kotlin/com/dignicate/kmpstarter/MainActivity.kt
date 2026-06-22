package com.dignicate.kmpstarter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dignicate.kmpstarter.core.PlatformInfo
import com.dignicate.kmpstarter.providers.startApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pkgInfo = packageManager.getPackageInfo(packageName, 0)
        startApplication(
            PlatformInfo(
                version = pkgInfo.versionName ?: "0.0.0",
                envName = BuildConfig.APP_ENV,
                packageName = packageName,
                isDebug = BuildConfig.DEBUG,
            ),
        )
        setContent {
            App()
        }
    }
}
