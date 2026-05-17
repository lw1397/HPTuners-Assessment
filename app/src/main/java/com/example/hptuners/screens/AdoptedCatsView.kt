package com.example.hptuners.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.State
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
import coil3.compose.AsyncImage
import com.example.hptuners.Edit
import com.example.hptuners.Status
import com.example.hptuners.UiState
import com.example.hptuners.data.adoptedCat.AdoptedCat
import com.example.hptuners.data.adoptedCat.AdoptedCatWithBreeds
import com.example.hptuners.data.breed.Breed
import com.example.hptuners.data.cat.Cat
import com.example.hptuners.data.cat.FindSomeCats
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

@Composable
fun AdoptedCatsScreen(
    nav: NavController,
    name: String,
    viewModel: AdoptedCatsViewModel = hiltViewModel(),
) {
    AdoptedCatsView(
        nav = nav,
        name = name,
        breeds = viewModel.breeds.collectAsStateWithLifecycle(),
        adoptedCats = viewModel.adoptedCats.collectAsStateWithLifecycle()
    )
}

@Composable
fun AdoptedCatsView(
    nav: NavController,
    name: String,
    breeds: State<UiState<List<Breed>>>,
    adoptedCats: State<UiState<List<AdoptedCatWithBreeds>>>
) {
    // TODO: Forms, how do they work? Is there API Endpoints for them? do we just force the user to type in the different values from memory?

    // TODO: Breed Search Dropdown

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
    ) {
        item {
            Text(
                text = "Hello $name!",
            )
        }
        item {
            if (breeds.value.status == Status.LOADING) {
                Text(text = "Loading Up The Breeds...")
            }
            if (!breeds.value.data.isNullOrEmpty()) {
                Text(
                    text = "Breed Names: ${breeds.value.data?.joinToString(", ") { it.name }}"
                )
            }
        }
        items(adoptedCats.value.data ?: listOf()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable( onClick = { nav.navigate(route = Edit(it.cat.id)) } ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "ID: ${it.cat.id}, Name: ${it.cat.name}, Breeds: ${
                        it.breeds.joinToString(
                            ""
                        ) { breed -> breed.name }
                    }"
                )
                AsyncImage(
                    model = it.cat.url,
                    contentDescription = "Picture for Cat ID: ${it.cat.id}",
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun NeighborhoodCatsPreview() {
    // TODO: NeighborhoodCatsView()
}