package com.dignicate.kmpstarter.data

import com.dignicate.kmpstarter.domain.TimeInfo
import com.dignicate.kmpstarter.domain.TimeRepository
import io.ktor.client.plugins.logging.Logger
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimeRepositoryImpl(
    private val apiClient: TimeApiClient,
    private val logger: Logger,
) : TimeRepository {

    override fun getCurrentTime(): Flow<Result<TimeInfo>> = flow {
        logger.log("[TimeRepository] getCurrentTime start")
        val result = try {
            val time = apiClient.getTime().toDomainObject()
            logger.log("[TimeRepository] getCurrentTime success")
            Result.success(time)
        } catch (e: CancellationException) {
            logger.log("[TimeRepository] getCurrentTime cancelled")
            throw e
        } catch (e: Exception) {
            logger.log("[TimeRepository] getCurrentTime error=${e.message}")
            Result.failure(e)
        }

        emit(result)
    }
}

private fun TimeDto.toDomainObject(): TimeInfo = TimeInfo(
    utc = utc,
    millis = millis,
    unixSeconds = unixSeconds,
    iso8601 = iso8601,
)
