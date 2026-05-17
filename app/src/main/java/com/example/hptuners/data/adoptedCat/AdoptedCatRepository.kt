package com.example.hptuners.data.adoptedCat

import com.example.hptuners.data.cat.Cat
import javax.inject.Inject

class AdoptedCatRepository @Inject constructor(
    private val adoptedCatDao: AdoptedCatDao
) {

    fun getAdoptedCats() = adoptedCatDao.getAllWithBreeds()

    suspend fun adoptACat(cat: Cat, name: String) {
        val adoptedCat = AdoptedCat(
            id = cat.id,
            name = name,
            url = cat.url,
            width = cat.width,
            height = cat.height
        )
        val refs = cat.breeds.map { CatBreedCrossRef(catId = cat.id, breedId = it.id) }

        adoptedCatDao.insert(adoptedCat)
        adoptedCatDao.insertCatBreedCrossRef(refs)
    }

}