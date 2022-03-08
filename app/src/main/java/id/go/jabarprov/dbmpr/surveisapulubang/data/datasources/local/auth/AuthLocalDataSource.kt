package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.local.auth

interface AuthLocalDataSource {
    suspend fun storeToken(token: String)
    fun getToken(): String
    suspend fun clearToken()
}