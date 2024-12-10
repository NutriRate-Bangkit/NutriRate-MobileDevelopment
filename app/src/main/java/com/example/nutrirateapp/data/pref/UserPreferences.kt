package com.example.nutrirateapp.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences(private val context: Context) {

    suspend fun saveSession(userId: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = userId
            preferences[EMAIL_KEY] = email
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGIN_KEY] ?: false
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val ID_KEY = stringPreferencesKey("user_id")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login")
    }
}