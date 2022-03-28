package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth

import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import id.go.jabarprov.dbmpr.surveisapulubang.core.exceptions.LocalDataSourceException
import id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.datastore.DataStorePreference
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import java.util.*
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(private val dataStorePreference: DataStorePreference) :
    AuthLocalDataSource {

    private val gson by lazy { Gson() }

    override suspend fun storeToken(token: String, tokenExpiredDate: Calendar) {
        dataStorePreference.apply {
            putString(TOKEN_KEY, token)
            putString(
                TOKEN_EXPIRED_DATE_KEY,
                CalendarUtils.formatCalendarToString(tokenExpiredDate)
            )
        }
    }

    override suspend fun storeUser(user: User) {
        val json = gson.toJson(user)
        dataStorePreference.putString(USER_KEY, json)
    }

    override fun getUser(): User {
        val json = dataStorePreference.getString(USER_KEY)
        return if (json.isNotBlank()) {
            gson.fromJson(json, User::class.java)
        } else {
            throw LocalDataSourceException("Silahkan Login Terlebih Dahulu")
        }
    }

    override fun getToken(): String {
        return dataStorePreference.getString(TOKEN_KEY)
    }

    override fun getTokenExpiredDate(): Calendar {
        val expiredDateString = dataStorePreference.getString(TOKEN_EXPIRED_DATE_KEY)
        return if (expiredDateString.isNotBlank()) {
            CalendarUtils.formatStringToCalendar(expiredDateString)
        } else {
            throw LocalDataSourceException("Silahkan Login Terlebih Dahulu")
        }
    }

    override suspend fun clearToken() {
        dataStorePreference.apply {
            deleteString(TOKEN_KEY)
            deleteString(TOKEN_EXPIRED_DATE_KEY)
            deleteString(USER_KEY)
        }
    }

    companion object {
        val TOKEN_KEY = stringPreferencesKey("TOKEN_KEY")
        val TOKEN_EXPIRED_DATE_KEY = stringPreferencesKey("TOKEN_EXPIRED_DATE")
        val USER_KEY = stringPreferencesKey("USER_KEY")
    }
}