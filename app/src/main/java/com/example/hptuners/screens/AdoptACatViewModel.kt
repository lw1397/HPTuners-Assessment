package com.example.hptuners.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hptuners.data.UserPreferences
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCatRepository
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.data.breed.BreedRepository
import com.example.hptuners.data.cat.Cat
import com.example.hptuners.data.cat.CatRepository
import com.example.hptuners.utils.randomCatNames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdoptACatViewModel @Inject constructor(
    private val catRepository: CatRepository,
    private val adoptedCatRepository: AdoptedCatRepository,
    private val userPreferences: UserPreferences,
    breedRepository: BreedRepository
): ViewModel() {

    val breedOptions: StateFlow<UiState<List<Breed>>> = breedRepository.getAllBreeds().map { breeds ->
        if (breeds.isEmpty()) {
            UiState.loading()
        } else {
            UiState.success(breeds)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.loading()
    )

    val preferredBreed: StateFlow<Breed?> = breedOptions.combine(userPreferences.preferredBreed) { breeds, pref ->
        if (breeds.data.isNullOrEmpty()) {
            null
        } else {
            breeds.data.firstOrNull { it.id == pref }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val catOptions: StateFlow<UiState<List<Cat>>> = preferredBreed.flatMapLatest {
        loadCats(it?.id)
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

    fun setPreferredBreed(breedId: String?) {
        viewModelScope.launch {
            userPreferences.setPreferredBreed(breedId ?: "")
        }
    }

    fun adoptACat(chosen: Cat, name: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            adoptedCatRepository.adoptACat(chosen, name.takeIf { it.isNotEmpty() } ?: randomCatNames.random())
            onSaved()
        }
    }
}