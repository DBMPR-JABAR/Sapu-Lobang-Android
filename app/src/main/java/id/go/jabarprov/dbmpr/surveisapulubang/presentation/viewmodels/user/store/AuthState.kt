package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User

data class AuthState(
    val user: User? = null,
    val isSuccess: Boolean = false,
    val isFailed: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : State