package com.example.hptuners.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.hptuners.Edit
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatRepository
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.utils.randomCatNames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditAdoptedCatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val adoptedCatRepository: AdoptedCatRepository
) : ViewModel() {
    val editParams = savedStateHandle.toRoute<Edit>()

    val catToEdit: StateFlow<UiState<AdoptedCatWithBreeds>> =
        adoptedCatRepository.getAdoptedCatById(editParams.id).map { cat ->
            UiState.success(cat)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.loading()
        )

    fun updateAdoptedCat(cat: AdoptedCat, newName: String?, earTipped: Boolean?, onUpdated: () -> Unit) {
        viewModelScope.launch {
            adoptedCatRepository.updateAdoptedCatInfo(
                cat.copy(
                    name = newName.takeIf { !it.isNullOrEmpty() } ?: randomCatNames.random(),
                    earTipped = earTipped ?: cat.earTipped
                )
            )
            onUpdated()
        }

    }
}