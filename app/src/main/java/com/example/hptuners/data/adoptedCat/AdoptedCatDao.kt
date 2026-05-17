package com.example.hptuners.data.adoptedCat

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface AdoptedCatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cat: AdoptedCat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatBreedCrossRef(crossRefs: List<CatBreedCrossRef>)

    @Transaction
    @Query("SELECT * FROM AdoptedCat")
    fun getAllWithBreeds(): Flow<List<AdoptedCatWithBreeds>>

    @Delete
    fun delete(cat: AdoptedCat)
}