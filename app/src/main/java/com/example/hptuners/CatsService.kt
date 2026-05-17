package com.example.hptuners

import androidx.room.Entity
import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class KtorCat(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val breeds: List<Breed>
)

@Entity
@Serializable
class Breed(
    val id: String,
    val name: String,
    val description: String,
    val temperament: String,
    val origin: String,
    @SerialName("country_code") val countryCode: String,
)

@Entity
@Serializable
class ExtendedBreed(
    val id: String,
    val name: String,
    val description: String,
    val temperament: String,
    val origin: String,
    @SerialName("country_code") val countryCode: String,
    @SerialName("wikipedia_url") val wiki: String? = null
)

@Serializable
@Resource("v1/breeds")
class GetAllBreeds()

@Serializable
@Resource("/v1/images/search")
class FindSomeCats(
    val limit: Int,
    @SerialName("has_breeds") val hasBreeds: Int,
    @SerialName("breed_ids") val breedIds: List<String>,
)