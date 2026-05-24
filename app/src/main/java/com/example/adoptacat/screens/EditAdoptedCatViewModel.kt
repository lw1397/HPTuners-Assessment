package com.example.adoptacat.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.adoptacat.Edit
import com.example.adoptacat.utils.UiState
import com.example.adoptacat.data.adoptedCat.AdoptedCat
import com.example.adoptacat.data.adoptedCat.AdoptedCatRepository
import com.example.adoptacat.data.adoptedCat.AdoptedCatWithBreeds
import com.example.adoptacat.utils.randomCatNames
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