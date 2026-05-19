package com.example.hptuners.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatRepository
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.data.breed.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AdoptedCatsViewModel @Inject constructor(
    private val adoptedCatRepository: AdoptedCatRepository,
): ViewModel() {
    val adoptedCats: StateFlow<UiState<List<AdoptedCatWithBreeds>>> = adoptedCatRepository.getAdoptedCats().map{ cats ->
        UiState.success(cats)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.loading()
    )

    fun removeAdoptedCat(cat: AdoptedCat) {
        viewModelScope.launch {
            adoptedCatRepository.removeAdoptedCat(cat)
        }
    }
}