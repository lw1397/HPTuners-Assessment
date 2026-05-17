package com.example.hptuners.data.cat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerialName
import javax.inject.Inject

class CatRepository @Inject constructor(
    private val ktorClient: HttpClient
) {
    suspend fun findSomeCats(
        limit: Int = 10,
        breedIds: List<String>? = null,
    ): List<Cat> {
        return ktorClient.get(FindSomeCats(limit, breedIds)).body()
    }
}