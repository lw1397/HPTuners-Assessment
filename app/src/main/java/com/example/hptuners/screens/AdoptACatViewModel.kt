package com.example.hptuners.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCatRepository
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.data.breed.BreedRepository
import com.example.hptuners.data.cat.Cat
import com.example.hptuners.data.cat.CatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    private val catRepository: CatRepository,
    private val adoptedCatRepository: AdoptedCatRepository,
    breedRepository: BreedRepository
): ViewModel() {

    val breedOptions: StateFlow<UiState<List<Breed>>> = breedRepository.getAllBreeds().map { breeds ->
        if (breeds.isEmpty()) {
            UiState.loading()
        } else {
            breeds.filter { it.temperament.length > 1 }.let {
                UiState.success(breeds)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.loading()
    )

    private val _breedFilter = MutableStateFlow<String?>(null)
    val breedFilter = _breedFilter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val catOptions: StateFlow<UiState<List<Cat>>> = _breedFilter.flatMapLatest {
        loadCats(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.loading()
    )

    private fun loadCats(breed: String?): Flow<UiState<List<Cat>>> = flow {
        emit(UiState.loading())
        val cats = if (breed.isNullOrEmpty()) {
            catRepository.findSomeCats()
        } else {
            catRepository.findSomeCats(breedIds = listOf(breed))
        }
        emit(UiState.success(cats))
    }

    fun setBreedFilter(breedId: String?) {
        _breedFilter.value = breedId
    }

    fun adoptACat(chosen: Cat, name: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            adoptedCatRepository.adoptACat(chosen, name.takeIf { it.isNotEmpty() } ?: randomCatNames.random())
            onSaved()
        }
    }
}