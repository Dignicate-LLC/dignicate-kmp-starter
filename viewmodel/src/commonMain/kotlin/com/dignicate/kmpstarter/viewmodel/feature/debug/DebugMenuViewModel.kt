package com.dignicate.kmpstarter.viewmodel.feature.debug

import androidx.lifecycle.ViewModel
import com.dignicate.kmpstarter.core.AppEnvironment
import com.dignicate.kmpstarter.core.AppConfig
import com.dignicate.kmpstarter.core.isDebugBuild
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DebugMenuViewModel(
    appConfig: AppConfig,
) : ViewModel() {
    data class UiState(
        val packageName: String,
        val version: String,
        val environment: AppEnvironment,
        val showsAppInfo: Boolean,
    )

    private val _uiState = MutableStateFlow(
        UiState(
            packageName = appConfig.packageName,
            version = appConfig.version,
            environment = appConfig.env,
            showsAppInfo = isDebugBuild(),
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}
