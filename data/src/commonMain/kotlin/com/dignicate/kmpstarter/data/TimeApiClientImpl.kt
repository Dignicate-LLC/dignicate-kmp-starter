package com.dignicate.kmpstarter.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.request.get

class TimeApiClientImpl(
    private val httpClient: HttpClient,
    private val logger: Logger,
) : TimeApiClient {

    override suspend fun getTime(): TimeDto {
        logger.log("[TimeApiClient] GET /time start")
        val response = httpClient
            .get("https://freeapi.dignicate.com/time/v1/current")
            .body<TimeDto>()
        logger.log("[TimeApiClient] GET /time done")
        return response
    }
}
