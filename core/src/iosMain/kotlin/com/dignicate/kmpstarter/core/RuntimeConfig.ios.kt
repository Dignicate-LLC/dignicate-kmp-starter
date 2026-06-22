package com.dignicate.kmpstarter.core

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual fun loadRuntimeConfigYaml(): String? {
    val path = NSBundle.mainBundle.pathForResource(
        name = "config",
        ofType = "yaml",
        inDirectory = "runtime",
    ) ?: return null

    return NSString.stringWithContentsOfFile(
        path = path,
        encoding = NSUTF8StringEncoding,
        error = null,
    ) as String?
}
