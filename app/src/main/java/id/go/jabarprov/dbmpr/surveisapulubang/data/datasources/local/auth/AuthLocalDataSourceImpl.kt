package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth

import androidx.datastore.preferences.core.stringPreferencesKey
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.datastore.DataStorePreference
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(private val dataStorePreference: DataStorePreference) :
    AuthLocalDataSource {
    override suspend fun storeToken(token: String) {
        dataStorePreference.putString(TOKEN_KEY, token)
    }

    override fun getToken(): String {
        return dataStorePreference.getString(TOKEN_KEY)
    }

    override suspend fun clearToken() {
        dataStorePreference.deleteString(TOKEN_KEY)
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("TOKEN_KEY")
    }
}