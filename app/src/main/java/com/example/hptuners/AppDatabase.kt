package com.example.hptuners

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatDao
import com.example.hptuners.data.adoptedCat.CatBreedCrossRef
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.data.breed.BreedDao
import kotlinx.serialization.json.Json

@Database(
    entities = [AdoptedCat::class, Breed::class, CatBreedCrossRef::class],
    version = 2
)
@TypeConverters(
    Converters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adoptedCatDao(): AdoptedCatDao
    abstract fun breedDao(): BreedDao
}

class Converters {
    @TypeConverter
    fun fromStringToStringList(value: String?): List<String>? {
        if (value == null) return null
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromListStringToString(list: List<String>?): String? {
        if (list == null) return null
        return Json.encodeToString(list)
    }
}