package com.dignicate.kmpstarter.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class TimeUseCase(private val repository: TimeRepository) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _trigger = MutableSharedFlow<Unit>()

    val data: StateFlow<Resource<TimeInfo>> = _trigger
        .debounce(300L)
        .flatMapLatest {
            repository.getCurrentTime().mapToResource()
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = Resource.Loading,
        )

    suspend fun fetch() {
        _trigger.emit(Unit)
    }
}
