package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth

import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import java.util.*

interface AuthLocalDataSource {
    suspend fun storeToken(token: String, tokenExpiredDate: Calendar)
    suspend fun storeUser(user: User)
    fun getUser(): User
    fun getToken(): String
    fun getTokenExpiredDate(): Calendar
    suspend fun clearToken()
}