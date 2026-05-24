package com.example.adoptacat

import kotlinx.serialization.Serializable

@Serializable object Home
@Serializable object AdoptedList
@Serializable object Add
@Serializable data class Edit(val id: String)
@Serializable object Wip
@Serializable object BreedList