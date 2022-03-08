package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

data class LoginResponse(
    val token: TokenResponse,
    val user: UserResponse
)
