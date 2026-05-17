package com.example.hptuners.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.hptuners.Status
import com.example.hptuners.UiState
import com.example.hptuners.data.cat.Cat

@Composable
fun AdoptACatScreen(
    nav: NavController,
    viewModel: AdoptACatViewModel = hiltViewModel()
) {
    AdoptACatView(
        nav = nav,
        catOptions = viewModel.catOptions.collectAsStateWithLifecycle(),
        adoptACat = { chosen -> viewModel.adoptACat(chosen) { nav.popBackStack() } }
    )
}

@Composable
fun AdoptACatView(
    nav: NavController,
    catOptions: State<UiState<List<Cat>>>,
    adoptACat: (Cat) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        stickyHeader {
            Text("Adoption Board")
        }
        when(catOptions.value.status) {
            Status.LOADING -> { item { Text("Searching the cat board...")}}
            Status.FAILURE -> { item { Text("Failed to load the list :(")}}
            Status.SUCCESS -> {
                if (catOptions.value.data.isNullOrEmpty()) {
                    item { Text("Looks like we don't have any cats for the breed")}
                }
                items(catOptions.value.data ?: listOf()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { adoptACat(it) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "ID: ${it.id}")
                        Text(text = "Names: ${it.breeds.joinToString("") { breed -> breed.name }}")
                        AsyncImage(
                            model = it.url,
                            contentDescription = "Picture for Cat ID: ${it.id}",
                            modifier = Modifier.height(40.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdoptACatPreview() {
    // TODO: AdoptACatView()
}