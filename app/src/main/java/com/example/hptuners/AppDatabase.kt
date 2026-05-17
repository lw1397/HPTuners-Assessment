package com.example.hptuners

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatDao
import com.example.hptuners.data.adoptedCat.CatBreedCrossRef
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.data.breed.BreedDao

@Database(
    entities = [AdoptedCat::class, Breed::class, CatBreedCrossRef::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adoptedCatDao(): AdoptedCatDao
    abstract fun breedDao(): BreedDao
}