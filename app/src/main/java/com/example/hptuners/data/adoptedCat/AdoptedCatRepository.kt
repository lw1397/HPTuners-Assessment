package com.example.hptuners.data.adoptedCat

import com.example.hptuners.data.cat.Cat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdoptedCatRepository @Inject constructor(
    private val adoptedCatDao: AdoptedCatDao
) {

    fun getAdoptedCats() = adoptedCatDao.getAllWithBreeds()
    fun getAdoptedCatById(id: String) = adoptedCatDao.getCatWithBreedById(id)

    suspend fun adoptACat(cat: Cat, name: String) {
        val adoptedCat = AdoptedCat(
            id = cat.id,
            name = name,
            url = cat.url,
            width = cat.width,
            height = cat.height,
            temperament = cat.breeds.flatMap { it.temperament.split(",") }
        )
        val refs = cat.breeds.map { CatBreedCrossRef(catId = cat.id, breedId = it.id) }

        adoptedCatDao.insert(adoptedCat)
        adoptedCatDao.insertCatBreedCrossRef(refs)
    }

    suspend fun updateAdoptedCatInfo(cat: AdoptedCat) {
        adoptedCatDao.update(cat)
    }

    suspend fun removeAdoptedCat(cat: AdoptedCat) {
        adoptedCatDao.delete(cat)
    }

}