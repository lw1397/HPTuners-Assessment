package com.example.hptuners.data.adoptedCat

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.hptuners.data.breed.Breed
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class AdoptedCat(
    @PrimaryKey val id: String,
    var name: String,
    val url: String,
    val width: Int,
    val height: Int,
    val temperament: List<String>,
    val earTipped: Boolean = false,
)

/*
 At Seerist, my preferred pattern was Denormalized list of IDs, but this relational is easier to setup

 Ex: Some reports would tag every country on a continent, resulting in 50+ countries worth of data for a single
 article in a list. Because you could search data by country, we already had a work manager run on login/app load
 to populate a Countries table with this information. When we processed an article, we would only save a list
 of the Alpha2 (or Alpha3 ?) codes. The first time the user looked up country data, we'd pull
 that info into an active variable in the Singleton Repo, and filter the countries from that list. This avoided
 relational DB calls, and it saved a lot on DB space.
*/
@Entity(
    primaryKeys = ["catId", "breedId"],
    foreignKeys = [
        ForeignKey(
            entity = AdoptedCat::class,
            parentColumns = ["id"],
            childColumns = ["catId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Breed::class,
            parentColumns = ["id"],
            childColumns = ["breedId"]
        )
    ]
)
data class CatBreedCrossRef(
    val catId: String,
    val breedId: String
)

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

