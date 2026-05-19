package com.example.hptuners.data.breed

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BreedRepository @Inject constructor(
    private val ktorClient: HttpClient,
    private val breedDao: BreedDao
) {

    suspend fun loadAllBreeds() {
        val breeds: List<Breed> = ktorClient.get(GetAllBreeds()).body()
        breedDao.insertAll(breeds)
    }

    fun getAllBreeds(): Flow<List<Breed>> = breedDao.getAllBreeds()
}