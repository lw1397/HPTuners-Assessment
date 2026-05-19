package com.example.hptuners.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun BreedListScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Example Secondary NavGraph",
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "I had grand plans to put the Breed List here, but ran out of time.",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "The vision was to make it searchable much like the Cat Adoption view, and you could click " +
                    "a breed to go to the Add page with that breed pre-filled.",
            textAlign = TextAlign.Center
        )
    }
}