package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.State
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User

data class AuthState(
    val userState: Resource<User> = Resource.Initial(),
    val checkTokenState: Resource<User> = Resource.Initial()
) : State