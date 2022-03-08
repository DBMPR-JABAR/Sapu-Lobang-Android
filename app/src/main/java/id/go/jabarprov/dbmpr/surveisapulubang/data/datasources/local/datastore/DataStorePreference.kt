package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

val Context.dataStorePref: DataStore<Preferences> by preferencesDataStore(name = "Preference DataStore")

class DataStorePreference @Inject constructor(@ApplicationContext private val context: Context) {

    fun getString(key: Preferences.Key<String>): String {
        return runBlocking {
            context.dataStorePref.data.map { pref -> pref[key] ?: "" }.first()
        }
    }

    suspend fun putString(key: Preferences.Key<String>, value: String) {
        context.dataStorePref.edit { pref ->
            pref[key] = value
        }
    }

    suspend fun deleteString(key: Preferences.Key<String>) {
        context.dataStorePref.edit { pref ->
            pref.remove(key)
        }
    }
}