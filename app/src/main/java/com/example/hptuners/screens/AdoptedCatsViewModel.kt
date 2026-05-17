package com.example.hptuners.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hptuners.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCatRepository
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.data.breed.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AdoptedCatsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    adoptedCatRepository: AdoptedCatRepository,
    private val breedRepository: BreedRepository
): ViewModel() {

    val breeds: StateFlow<UiState<List<Breed>>> = breedRepository.getAllBreeds().map { breeds ->
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

    val adoptedCats: StateFlow<UiState<List<AdoptedCatWithBreeds>>> = adoptedCatRepository.getAdoptedCats().map{ cats ->
        UiState.success(cats)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.loading()
    )

    init {
        viewModelScope.launch {
            breedRepository.loadAllBreeds()
        }
    }
}