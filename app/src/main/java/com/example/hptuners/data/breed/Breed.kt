package com.example.hptuners.data.breed

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ktor.resources.Resource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Breed(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val temperament: String,
    val origin: String,
    @SerialName("country_code") val countryCode: String,
    @SerialName("wikipedia_url") val wiki: String? = null
)

@Serializable
@Resource("v1/breeds")
class GetAllBreeds

