package com.example.leannextfull.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**Хранилище данных*/
class StoreData(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by  preferencesDataStore("userName")
        private val USERNAME  = stringPreferencesKey("user_name") //Параметр для сохранения имя пользователя
    }

    val getData: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }


    suspend fun saveData(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME ] = name
        }
    }
}