package com.example.adoptacat.data.adoptedCat

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AdoptedCatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cat: AdoptedCat)

    @Update
    suspend fun update(cat: AdoptedCat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatBreedCrossRef(crossRefs: List<CatBreedCrossRef>)

    @Transaction
    suspend fun insertAdoptedCatAndBreeds(
        cat: AdoptedCat, refs: List<CatBreedCrossRef>
    ) {
        insert(cat)
        insertCatBreedCrossRef(refs)
    }

    @Transaction
    @Query("SELECT * FROM AdoptedCat")
    fun getAllWithBreeds(): Flow<List<AdoptedCatWithBreeds>>

    @Transaction
    @Query("Select * FROM AdoptedCat WHERE id = :id")
    fun getCatWithBreedById(id: String): Flow<AdoptedCatWithBreeds>

    @Delete
    suspend fun delete(cat: AdoptedCat)
}