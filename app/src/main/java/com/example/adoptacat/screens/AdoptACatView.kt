package com.example.adoptacat.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.SubcomposeAsyncImage
import com.example.adoptacat.utils.Status
import com.example.adoptacat.utils.UiState
import com.example.adoptacat.data.breed.Breed
import com.example.adoptacat.data.cat.Cat
import com.example.adoptacat.utils.LoadingAsyncImage
import com.example.adoptacat.utils.PreviewUtils

@Composable
fun AdoptACatScreen(
    nav: NavController,
    viewModel: AdoptACatViewModel = hiltViewModel()
) {
    AdoptACatView(
        nav = nav,
        breeds = viewModel.breedOptions.collectAsStateWithLifecycle(),
        preferredBreed = viewModel.preferredBreed.collectAsStateWithLifecycle(),
        setBreed = { breed -> viewModel.setPreferredBreed(breed?.id) },
        catOptions = viewModel.catOptions.collectAsStateWithLifecycle(),
        adoptACat = { chosen, name, callback -> viewModel.adoptACat(chosen, name) { callback() } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptACatView(
    nav: NavController,
    breeds: State<UiState<List<Breed>>>,
    preferredBreed: State<Breed?>,
    setBreed: (Breed?) -> Unit,
    catOptions: State<UiState<List<Cat>>>,
    adoptACat: (Cat, String, () -> Unit) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var selectedCat by remember { mutableStateOf<Cat?>(null) }

    LazyColumn(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        item {
            Text("Adoption Board")
            breeds.value.data?.let {
                SearchableExpandedDropDownMenu(
                    options = it,
                    selected = preferredBreed,
                    setBreed = setBreed
                )
            }
        }
        when(catOptions.value.status) {
            Status.LOADING -> { item { Text("Searching the cat board...", modifier = Modifier.padding(4.dp))}}
            Status.FAILURE -> { item { Text("Failed to load the list :(", modifier = Modifier.padding(4.dp))}}
            Status.SUCCESS -> {
                if (catOptions.value.data.isNullOrEmpty()) {
                    item { Text("Looks like we don't have any cats for the breed")}
                }

                items(catOptions.value.data ?: listOf()) { cat ->
                    Surface(
                        tonalElevation = 2.dp,
                        modifier = Modifier.padding(4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceDim),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    selectedCat = cat
                                    showSheet = true
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(text = "Breed: ${cat.breeds.joinToString(", ") { breed -> breed.name }}")
                                Text(text = "Origin: ${cat.breeds.joinToString { breed -> breed.origin }}")
                            }

                            LoadingAsyncImage(cat.url, cat.id, Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    if (showSheet && selectedCat != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
        ) {
            val catName = rememberTextFieldState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    state = catName,
                    label = { Text("You New Friend's Name") },
                    placeholder = { Text("(Leave Blank for Random Name)") }
                )

                Button (
                    onClick = {
                        selectedCat?.let { cat ->
                            adoptACat(cat, catName.text.toString()) {
                                showSheet = false
                                nav.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Adopt", color = MaterialTheme.colorScheme.onPrimary)
                }

                OutlinedButton (
                    onClick = { showSheet = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

// Lifted this dropdown from the internet to save time
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableExpandedDropDownMenu(
    options: List<Breed>,
    selected: State<Breed?>,
    setBreed: (Breed?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(selected.value?.name ?: "") }

    // Filter items based on query
    val filteredOptions = options.filter { it.name.contains(searchQuery, ignoreCase = true) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.padding(4.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                expanded = true // Keep open while typing
            },
            label = { Text("Search Breeds") },
            placeholder = { Text("Optional Breed Selection") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, true)
        )

        if (filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Random") },
                    onClick = {
                        searchQuery = ""
                        setBreed(null)
                        expanded = false
                    }
                )
                filteredOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.name) },
                        onClick = {
                            searchQuery = selectionOption.name
                            setBreed(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdoptACatPreview() {
    MaterialTheme {
        AdoptACatView(
            nav = rememberNavController(),
            breeds = remember { PreviewUtils.uiSuccessState(listOf()) },
            preferredBreed = remember { mutableStateOf(null) },
            setBreed = { _ -> },
            catOptions = remember { PreviewUtils.uiSuccessState((List(10) { PreviewUtils.cat })) },
            adoptACat = { _, _, _, -> },

        )
    }

}