package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Action

sealed class AuthAction : Action {
    class LoginUserAction(val username: String, val password: String) : AuthAction()
    object LogoutUserAction : AuthAction()
}