package com.example.hptuners.data.cat

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cat(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val breeds: List<BasicBreed>
)

@Serializable
data class BasicBreed(
    val id: String,
    val name: String,
    val description: String,
    val temperament: String,
    val origin: String,
)

enum class CatApiSortOrder { RAND, ASC, DESC }

@Serializable
@Resource("/v1/images/search")
class FindSomeCats(
    val limit: Int,
    @SerialName("breed_ids") val breedIds: List<String>? = null,
    @SerialName("has_breeds") val hasBreeds: Int = 1,
    val order: CatApiSortOrder = CatApiSortOrder.RAND,
)
