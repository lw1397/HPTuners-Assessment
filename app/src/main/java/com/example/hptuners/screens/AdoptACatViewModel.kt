package com.example.hptuners.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hptuners.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCatRepository
import com.example.hptuners.data.cat.Cat
import com.example.hptuners.data.cat.CatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

val randomCatNames = listOf(
    "Tater Tot",
    "Macaroni",
    "Siracha",
    "Garlic Bread",
    "Cheddar Goblin",
    "Sir Pounce-a-Lot",
    "Count Fluffula",
    "Professor Wigglebottom",
    "Chairman Meow",
    "Lady Paw",
    "Gremlin",
    "Mayhem",
    "Goblin",
    "Waffles the Destroyer",
    "Chaos Machine",
    "Gary",
    "Brenda",
    "Kevin",
    "Walter",
    "Craig",
    "Bootsy Collins",
    "Noodle",
    "Socks (but only has three)",
    "Doink",
    "Meowly Cyrus"
)

@HiltViewModel
class AdoptACatViewModel @Inject constructor(
    val catRepository: CatRepository,
    val adoptedCatRepository: AdoptedCatRepository
): ViewModel() {

    val _catOptions = MutableStateFlow<UiState<List<Cat>>>(UiState.loading())
    val catOptions = _catOptions.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val cats = catRepository.findSomeCats()
            _catOptions.update { UiState.success(cats) }
        }
    }

    fun adoptACat(chosen: Cat, name: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            adoptedCatRepository.adoptACat(chosen, name.takeIf { it.isNotEmpty() } ?: randomCatNames.random())
            onSaved()
        }
    }
}