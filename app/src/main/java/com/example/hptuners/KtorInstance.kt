package com.example.hptuners

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object KtorInstance {

    private const val BASE_URL = "https://api.thecatapi.com"

    fun getInstance() =
        HttpClient(CIO) {
            install(Resources)
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true // Prevents crashes if API adds new fields
                })
            }
            install(DefaultRequest) {
                url(BASE_URL)
                header("x-api-key", BuildConfig.CATS_API_KEY)
            }

        }

}