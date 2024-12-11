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

    suspend fun saveSession(userId: String, email: String, token: String) {
        context.dataStore.edit { preferences ->
            preferences[ID_KEY] = userId
            preferences[EMAIL_KEY] = email
            preferences[TOKEN_KEY] = token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGIN_KEY] ?: false
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            val token = preferences[TOKEN_KEY]
            token
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
        private val TOKEN_KEY = stringPreferencesKey("user_token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login")
    }
}