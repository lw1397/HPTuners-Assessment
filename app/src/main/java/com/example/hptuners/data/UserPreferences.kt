package com.example.hptuners.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder { ASC, DESC }
enum class SortField { NAME, BREED }

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val SORT_FIELD = stringPreferencesKey("sort_field")
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val PREFERRED_BREED = stringPreferencesKey("preferred_breed")
    }

    val sortField = dataStore.data.map { it[SORT_FIELD]?.let { SortField.valueOf(it) } ?: SortField.NAME }
    val sortOrder = dataStore.data.map { it[SORT_ORDER]?.let { SortOrder.valueOf(it) } ?: SortOrder.ASC }

    val preferredBreed = dataStore.data.map { it[PREFERRED_BREED] ?: "" }

    suspend fun setSortField(value: SortField) {
        dataStore.edit { it[SORT_FIELD] = value.name }
    }

    suspend fun setSortOrder(value: SortOrder) {
        dataStore.edit { it[SORT_ORDER] = value.name }
    }

    suspend fun setPreferredBreed(id: String) {
        dataStore.edit { it[PREFERRED_BREED] = id }
    }
}