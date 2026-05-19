package com.example.hptuners.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hptuners.data.SortField
import com.example.hptuners.data.SortOrder
import com.example.hptuners.data.UserPreferences
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatRepository
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.data.cat.CatApiSortOrder
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
    private val userPreferences: UserPreferences
): ViewModel() {

    val sortField = userPreferences.sortField.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SortField.NAME
    )

    val sortOrder = userPreferences.sortOrder.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SortOrder.ASC
    )

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

    fun setSortField(field: SortField) {
        viewModelScope.launch {
            userPreferences.setSortField(field)
        }
    }

    fun setSortOrder(order: SortOrder) {
        viewModelScope.launch {
            userPreferences.setSortOrder(order)
        }
    }
}