package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.CheckAccessToken
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.LoginUser
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.LogoutUser
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthStore @Inject constructor(
    private val loginUser: LoginUser,
    private val logoutUser: LogoutUser,
    private val checkAccessToken: CheckAccessToken
) :
    Store<AuthAction, AuthState>(AuthState()) {
    override fun reduce(action: AuthAction) {
        coroutineScope.launch {
            when (action) {
                is AuthAction.LoginUserAction -> loginUser(action)
                AuthAction.LogoutUserAction -> logoutUser()
                AuthAction.CheckAccessToken -> checkAccessToken()
            }
        }
    }

    private suspend fun loginUser(action: AuthAction.LoginUserAction) {
        setLoadingState()
        val loginParam = LoginUser.Params(action.username, action.password)
        val result = loginUser.run(loginParam)
        result.either(
            fnL = { failure ->
                setErrorState(failure.message)
            },
            fnR = { user ->
                setUserState(user)
            },
        )
    }

    private suspend fun logoutUser() {
        setLoadingState()
        val result = logoutUser.run(None)
        result.either(
            fnL = { failure ->
                setErrorState(failure.message)
            },
            fnR = {
                setInitialState()
            },
        )
    }

    private suspend fun checkAccessToken() {
        val result = checkAccessToken.run(None)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(checkTokenState = Resource.Failed(failure.message))
            },
            fnR = { user ->
                state.value = state.value.copy(
                    checkTokenState = Resource.Success(user),
                    userState = Resource.Success(user)
                )
            },
        )
    }

    private fun setInitialState() {
        state.value = state.value.copy(
            userState = Resource.Initial()
        )
    }

    private fun setUserState(user: User) {
        state.value = state.value.copy(userState = Resource.Success(user))
    }

    private fun setLoadingState() {
        state.value = state.value.copy(userState = Resource.Loading())
    }

    private fun setErrorState(errorMessage: String) {
        state.value = state.value.copy(userState = Resource.Failed(errorMessage))
    }
}