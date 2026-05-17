package com.example.hptuners

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hptuners.screens.Greeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HpTunersApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Louis' HP Tuners List")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Home) }
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                    IconButton(
                        onClick = { navController.navigate(Wip) }
                    ) {
                        Icon(Icons.Default.Build, contentDescription = "Work In Progress")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(Edit) }
                    ) {
                        Icon(  Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        },
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Home, modifier = Modifier.padding(innerPadding)) {
            composable<Home> {
                Greeting(name = "Android")
            }
            composable<Edit> {
                Text("you've gone to the Edit page")
            }
            composable<Wip> {
                Text("Work In Progress")
            }
        }

    }
}