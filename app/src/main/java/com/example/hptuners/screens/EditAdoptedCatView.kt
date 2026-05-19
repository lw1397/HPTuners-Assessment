package com.example.hptuners.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.hptuners.utils.Status
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.data.adoptedCat.CatBreedCrossRef
import com.example.hptuners.utils.PreviewUtils


@Composable
fun EditAdoptedCatScreen(
    nav: NavController,
    viewModel: EditAdoptedCatViewModel = hiltViewModel()
) {
    EditAdoptedCatView(
        cat = viewModel.catToEdit.collectAsStateWithLifecycle(),
        updateAdoptedCat = { cat, newName, earTipped ->
            viewModel.updateAdoptedCat(cat, newName, earTipped) {
                nav.popBackStack()
            }
        }
    )
}

@Composable
fun EditAdoptedCatView(
    cat: State<UiState<AdoptedCatWithBreeds>>,
    updateAdoptedCat: (AdoptedCat, String?, Boolean?) -> Unit
) {
    Column {
        if (cat.value.status != Status.LOADING) {
            cat.value.data?.cat?.let { cat ->
                val newName = rememberTextFieldState(cat.name)
                var earTipped by remember { mutableStateOf(cat.earTipped) }

                TextField(
                    state = newName,
                    label = { Text("Rename Your friend") },
                    placeholder = { Text("(Leave Blank for Random Name)") }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ear-Tipped: ")
                    Switch(
                        checked = earTipped,
                        onCheckedChange = {
                            earTipped = it
                        },
                    )
                }
                Button (
                    onClick = {
                        updateAdoptedCat(cat, newName.text.toString(), earTipped)
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Save Changes", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditAdoptedCatPreview() {
    MaterialTheme {
        EditAdoptedCatView(
            cat = remember {
                PreviewUtils.uiSuccessState(AdoptedCatWithBreeds(
                    cat = PreviewUtils.adoptedCat,
                    breeds = listOf(PreviewUtils.breed))
                )
           },
            updateAdoptedCat = { _, _, _ -> }
        )
    }
}