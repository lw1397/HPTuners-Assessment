package com.example.hptuners.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.hptuners.HpTunersApp
import com.example.hptuners.ExtendedBreed
import com.example.hptuners.FindSomeCats
import com.example.hptuners.GetAllBreeds
import com.example.hptuners.KtorCat
import com.example.hptuners.KtorInstance
import com.example.hptuners.ui.theme.HPTunersTheme
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get

@Composable
fun Greeting(name: String) {
    val ktorCall = KtorInstance.getInstance()

    var catsResponse by remember { mutableStateOf(emptyList<KtorCat>()) }
    var breedsResponse by remember { mutableStateOf(emptyList<ExtendedBreed>()) }


    // TODO: Move this to a repository, have it save off to RoomDB maybe?

    // TODO: Forms, how do they work? Is there API Endpoints for them? do we just force the user to type in the different values from memory?

    // TODO: MVVM Architecture, move this into a separate area for that

    // TODO: Compose Navigation


    // Breed Search Dropdown

    LaunchedEffect(true) {
        breedsResponse = ktorCall.get(GetAllBreeds()).body()
        catsResponse = ktorCall.get(
            FindSomeCats(
                limit = 10,
                hasBreeds = 1,
                breedIds = listOf("beng", "abys")
            )
        ).body()
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        item {
            Text(
                text = "Hello $name!",
            )
        }
        item {
            if (breedsResponse.isNotEmpty()) {
                Text(
                    text = "Breed Names: ${breedsResponse.joinToString(", ") { it.name }}"
                )
            }
        }
        items(catsResponse) { cat ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID: ${cat.id}"
                )
                Text(
                    text = "Names: ${cat.breeds.joinToString("") { it.name }}"
                )
                AsyncImage(
                    model = cat.url,
                    contentDescription = "Picture for Cat ID: ${cat.id}",
                    modifier = Modifier.height(40.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HPTunersTheme {
        HpTunersApp()
    }
}