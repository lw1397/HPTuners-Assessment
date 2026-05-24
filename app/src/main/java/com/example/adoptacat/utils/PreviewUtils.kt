package com.example.adoptacat.utils

import androidx.compose.runtime.mutableStateOf
import com.example.adoptacat.data.adoptedCat.AdoptedCat
import com.example.adoptacat.data.breed.Breed
import com.example.adoptacat.data.cat.BasicBreed
import com.example.adoptacat.data.cat.Cat

class PreviewUtils {
    companion object {

        val basicBreed = BasicBreed(
            id = "abys",
            name = "Abyssinian",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            temperament = "Active, Energetic, Independent, Intelligent, Gentle",
            origin = "Egypt",
        )

        val breed = Breed(
            id = "abys",
            name = "Abyssinian",
            description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
            temperament = "Active, Energetic, Independent, Intelligent, Gentle",
            origin = "Egypt",
            countryCode = "EG",
            wiki = "https://en.wikipedia.org/wiki/Abyssinian_(cat)"
        )

        val cat = Cat(
            id = "1",
            url = "https://cdn2.thecatapi.com/images/7isAO4Cav.jpg",
            width = 960,
            height = 960,
            breeds = listOf(basicBreed)
        )

        val adoptedCat = AdoptedCat(
            id = "1",
            name = "Sherlock",
            url = "https://cdn2.thecatapi.com/images/7isAO4Cav.jpg",
            width = 960,
            height = 960,
            temperament = listOf("Active", "Energetic", "Independent", "Intelligent", "Gentle"),
        )

        fun <T> uiSuccessState(data: T) = mutableStateOf(UiState.success(data))
    }
}