package com.example.hptuners

import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Add
@Serializable
data class Edit(val id: String)
@Serializable
object Wip