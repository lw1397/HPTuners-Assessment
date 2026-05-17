package com.example.hptuners.data.adoptedCat

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.hptuners.data.breed.Breed
import kotlinx.serialization.Serializable

@Entity
@Serializable
class AdoptedCat(
    @PrimaryKey val id: String,
    var name: String? = null,
    val url: String,
    val width: Int,
    val height: Int
)

@Entity(primaryKeys = ["catId", "breedId"])
data class CatBreedCrossRef(
    val catId: String,
    val breedId: String
)

/*
 At Seerist, my preferred pattern was Denormalized list of IDs so I didn't need a relational table

 Ex: All data had a list of country information, I would store the Alpha2 in a list and look it up
 from a "config" table I loaded with a worker. Because we were doing A LOT of geo data, I would also
 cache that value at the Repo layer when you first loaded in so that you weren't constantly hitting the DB.

 So the final pattern would be:
 1) When logging in, explicitly call and wait for countries
 2) On subsequent loads, start a background worker to query, parse, and update the countries
 3) The first time you request the country info, load it into a cached list on the Singleton for Country Lookups
 4) Look up the country from the cached list, attach it to the info

*/
data class AdoptedCatWithBreeds(
    @Embedded val cat: AdoptedCat,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CatBreedCrossRef::class,
            parentColumn = "catId",
            entityColumn = "breedId"
        )
    )
    val breeds: List<Breed>
)

