package com.example.hptuners.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.hptuners.Edit
import com.example.hptuners.utils.Status
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.data.adoptedCat.CatBreedCrossRef
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.utils.PreviewUtils
import kotlinx.coroutines.launch

@Composable
fun AdoptedCatsScreen(
    nav: NavController,
    viewModel: AdoptedCatsViewModel = hiltViewModel(),
) {
    AdoptedCatsView(
        nav = nav,
        adoptedCatsState = viewModel.adoptedCats.collectAsStateWithLifecycle(),
        removeAdoptedCat = { cat: AdoptedCat -> viewModel.removeAdoptedCat(cat) }
    )
}

@Composable
fun AdoptedCatsView(
    nav: NavController,
    adoptedCatsState: State<UiState<List<AdoptedCatWithBreeds>>>,
    removeAdoptedCat: (AdoptedCat) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var search by rememberSaveable { mutableStateOf("") }
    var nameBreedToggle by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = search,
                    onValueChange = {
                        search = it
                    },
                    label = { Text("Search") },
                    placeholder = { Text("(Name or Breed)") },
                    modifier = Modifier.weight(2f)
                )
                Column(
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Name <-> Breed")
                    Switch(
                        checked = nameBreedToggle,
                        onCheckedChange = {
                            nameBreedToggle = !nameBreedToggle
                        },
                    )
                }
            }

        }
        adoptedCatsState.value.data?.filter {
            if (search.isNotEmpty()) {
                if(nameBreedToggle) {
                    it.breeds.any { breed -> breed.name.contains(search, ignoreCase = true)}
                } else {
                    it.cat.name.contains(search, ignoreCase = true)
                }
            } else {
                true
            }
        }?.let { adoptedCats ->
            items(
                items = adoptedCats,
                key = { it.cat.id }
            ) {
                var expanded by remember { mutableStateOf(false) }

                val dismissState = rememberSwipeToDismissBoxState(
                    positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold)

                SwipeToDismissBox(
                    state = dismissState,
                    onDismiss = { dismissValue ->
                        when (dismissValue) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                coroutineScope.launch {
                                    dismissState.reset()
                                    nav.navigate(Edit(it.cat.id))
                                }
                            }
                            SwipeToDismissBoxValue.EndToStart -> {
                                coroutineScope.launch {
                                    dismissState.reset()
                                    removeAdoptedCat(it.cat)
                                }
                            }
                            SwipeToDismissBoxValue.Settled -> {
                                // no action
                            }
                        }
                    },
                    backgroundContent = {
                        when (dismissState.dismissDirection) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .wrapContentSize(Alignment.CenterStart)
                                        .padding(12.dp),
                                    tint = Color.White
                                )
                            }
                            SwipeToDismissBoxValue.EndToStart -> {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove item",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.onErrorContainer)
                                        .wrapContentSize(Alignment.CenterEnd)
                                        .padding(12.dp),
                                    tint = MaterialTheme.colorScheme.onError
                                )
                            }
                            SwipeToDismissBoxValue.Settled -> {}
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable(
                                onClick = {
                                    expanded = !expanded
                                    println("expanded: $expanded")
                                }
                            ),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier.weight(2f)
                            ) {
                                Text(
                                    text = "ID: ${it.cat.id}, Name: ${it.cat.name}, Breeds: ${
                                        it.breeds.joinToString(
                                            ""
                                        ) { breed -> breed.name }
                                    }"
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                AsyncImage(
                                    model = it.cat.url,
                                    contentDescription = "Picture for Cat ID: ${it.cat.id}"
                                )
                            }
                        }
                        if (expanded) {
                            Column {
                                Text(
                                    text = "Ear-Tipped: ${it.cat.earTipped}",
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = "Temperaments: ${it.cat.temperament.joinToString(", ")}",
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdoptedCatsPreview() {
    MaterialTheme {
        AdoptedCatsView(
            nav = rememberNavController(),
            adoptedCatsState = remember { mutableStateOf(UiState.success(List(6) { i ->
                AdoptedCatWithBreeds(PreviewUtils.adoptedCat.copy(id = "$i"), listOf(PreviewUtils.breed))
            }))},
            removeAdoptedCat = { _ -> }
        )
    }
}