package com.naufal.capstonewasteclassification.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val token_key = stringPreferencesKey("token")
    private val firstTime = booleanPreferencesKey("first_time")

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[token_key] ?: "null"
        }
    }

    fun isFirstTime(): Flow<Boolean> {
        return dataStore.data.map {
            it[firstTime] ?: true
        }
    }
    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[token_key] = token
        }
    }
    suspend fun setFirstLogin(firstLogin: Boolean) {
        dataStore.edit {
            it[this.firstTime] = firstLogin
        }
    }
    suspend fun deleteToken() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val USERID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
