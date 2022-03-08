package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.auth

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.LoginResponse

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): LoginResponse
}