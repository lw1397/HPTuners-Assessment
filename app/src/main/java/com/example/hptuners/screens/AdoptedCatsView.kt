package com.example.hptuners.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.hptuners.Add
import com.example.hptuners.Edit
import com.example.hptuners.data.SortField
import com.example.hptuners.data.SortOrder
import com.example.hptuners.utils.Status
import com.example.hptuners.utils.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.data.adoptedCat.CatBreedCrossRef
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.utils.LoadingAsyncImage
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
        removeAdoptedCat = { cat: AdoptedCat -> viewModel.removeAdoptedCat(cat) },
        sortOrder = viewModel.sortOrder.collectAsStateWithLifecycle(),
        setSortOrder = { order -> viewModel.setSortOrder(order) },
        sortField = viewModel.sortField.collectAsStateWithLifecycle(),
        setSortField = { field -> viewModel.setSortField(field) }
    )
}

@Composable
fun AdoptedCatsView(
    nav: NavController,
    adoptedCatsState: State<UiState<List<AdoptedCatWithBreeds>>>,
    removeAdoptedCat: (AdoptedCat) -> Unit,
    sortOrder: State<SortOrder>,
    setSortOrder: (SortOrder) -> Unit,
    sortField: State<SortField>,
    setSortField: (SortField) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    var search by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
    ) {
        ->
        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    TextField(
                        value = search,
                        onValueChange = { search = it },
                        label = { Text("Search") },
                        placeholder = { Text("(Name or Breed)") },
                        modifier = Modifier.weight(2f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Name <-> Breed")
                        Switch(
                            checked = sortField.value == SortField.BREED,
                            onCheckedChange = {
                                if(sortField.value == SortField.BREED) setSortField(SortField.NAME)
                                else setSortField(SortField.BREED)
                            },
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Asc <-> Desc")
                        Switch(
                            checked = sortOrder.value == SortOrder.DESC,
                            onCheckedChange = {
                                if (sortOrder.value == SortOrder.DESC) setSortOrder(SortOrder.ASC)
                                else setSortOrder(SortOrder.DESC)
                            },
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("(Swipe Left to Delete, Swipe Right to Edit)")
                }
            }
        }
        adoptedCatsState.value.data?.takeIf { it.isNotEmpty() }?.filter {
            if (search.isNotEmpty()) {
                if(sortField.value == SortField.BREED) {
                    it.breeds.any { breed -> breed.name.contains(search, ignoreCase = true)}
                } else {
                    it.cat.name.contains(search, ignoreCase = true)
                }
            } else {
                true
            }
        }?.let { filtered ->
            if (sortOrder.value == SortOrder.DESC) {
                filtered.sortedByDescending { if (sortField.value == SortField.BREED) it.breeds.firstOrNull()?.name else it.cat.name }
            } else {
                filtered.sortedBy { if (sortField.value == SortField.BREED) it.breeds.firstOrNull()?.name else it.cat.name }
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
                    modifier = Modifier.padding(vertical = 4.dp),
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
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            border = BorderStroke(1.dp, Color.Transparent),
                                            shape = RoundedCornerShape(12.dp)
                                        )
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
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            border = BorderStroke(1.dp, Color.Transparent),
                                            shape = RoundedCornerShape(12.dp)
                                        )
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
                    Surface(
                        tonalElevation = 2.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceDim), // Defines thickness and color
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
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
                                        text = "Name: ${it.cat.name}"
                                    )
                                    Text(
                                        text = "Breed: ${it.breeds.joinToString("") { breed -> breed.name }}"
                                    )
                                }
                                LoadingAsyncImage(it.cat.url, it.cat.id, Modifier.weight(1f))
                            }
                            if (expanded) {
                                Column {
                                    it.breeds.forEach { breed ->
                                        Text(
                                            text = "${breed.name}: ${breed.description}",
                                            color = MaterialTheme.colorScheme.tertiary                                        )
                                    }
                                    Text(
                                        text = "Temperaments: ${it.cat.temperament.joinToString(", ")}",
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = "Ear-Tipped: ${it.cat.earTipped}",
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } ?: item {
            Surface(
                tonalElevation = 1.dp,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceDim), // Defines thickness and color
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { nav.navigate(route = Add) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(  Icons.Default.Add, contentDescription = "Add")
                    Text(
                        text = "To get started, tap here or use the Floating Action Button to adopt your first Cat!",
                        textAlign = TextAlign.Center
                    )
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
            adoptedCatsState = remember { PreviewUtils.uiSuccessState(List(6) { i ->
                AdoptedCatWithBreeds(PreviewUtils.adoptedCat.copy(id = "$i"), listOf(PreviewUtils.breed))
            })},
            removeAdoptedCat = { _ -> },
            sortField = remember { mutableStateOf(SortField.BREED) },
            setSortField = { _ -> },
            sortOrder = remember { mutableStateOf(SortOrder.DESC) },
            setSortOrder = { _ -> }
        )
    }
}