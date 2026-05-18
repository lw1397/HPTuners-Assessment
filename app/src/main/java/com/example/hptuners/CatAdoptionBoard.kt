package com.example.hptuners

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.navigation.toRoute
import com.example.hptuners.screens.AdoptACatScreen
import com.example.hptuners.screens.AdoptedCatsScreen
import com.example.hptuners.screens.EditAdoptedCatScreen
import com.example.hptuners.screens.EditAdoptedCatView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatAdoptionBoard() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Cat Adoption Board")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            println("we're trying to go backwards")
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
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
                        onClick = { navController.navigate(route = Add) }
                    ) {
                        Icon(  Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        },
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Home, modifier = Modifier.padding(innerPadding)) {
            composable<Home> {
                AdoptedCatsScreen(nav = navController)
            }
            composable<Edit>(
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { fadeOut() }
            ) {
                EditAdoptedCatScreen(nav = navController)
            }
            composable<Add>(
                enterTransition = { slideInHorizontally { -it } }
            ) {
                AdoptACatScreen(nav = navController)
            }
            composable<Wip> {
                Text("Work In Progress", modifier = Modifier.fillMaxSize())
            }
        }

    }
}