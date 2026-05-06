package com.dignicate.kmpstarter.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
}

fun <T> Flow<Result<T>>.mapToResource(): Flow<Resource<T>> = map { result ->
    result.fold(
        onSuccess = { Resource.Success(it) },
        onFailure = { Resource.Error(it) }
    )
}
