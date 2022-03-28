package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
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
                is AuthAction.LoginUserAction -> {
                    state.value = state.value.copy(
                        user = null,
                        isSuccess = false,
                        isFailed = false,
                        isLoading = true,
                        errorMessage = null,
                    )
                    val loginParam = LoginUser.Params(action.username, action.password)
                    val result = loginUser.run(loginParam)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                user = null,
                                isSuccess = false,
                                isFailed = true,
                                isLoading = false,
                                errorMessage = failure.message
                            )
                        },
                        fnR = { user ->
                            state.value = state.value.copy(
                                user = user,
                                isSuccess = true,
                                isFailed = false,
                                isLoading = false,
                                errorMessage = null
                            )
                        },
                    )
                }

                AuthAction.LogoutUserAction -> {
                    state.value = state.value.copy(
                        isSuccess = false,
                        isFailed = false,
                        isLoading = true,
                        errorMessage = null,
                    )
                    val result = logoutUser.run(None)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isSuccess = false,
                                isFailed = true,
                                isLoading = false,
                                errorMessage = failure.message
                            )
                        },
                        fnR = {
                            state.value = state.value.copy(
                                user = null,
                                isSuccess = true,
                                isFailed = false,
                                isLoading = false,
                                errorMessage = null
                            )
                        },
                    )
                }

                AuthAction.CheckAccessToken -> {
                    val result = checkAccessToken.run(None)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isSuccess = false,
                                isFailed = true,
                                isLoading = false,
                                errorMessage = failure.message
                            )
                        },
                        fnR = { user ->
                            state.value = state.value.copy(
                                user = user,
                                isSuccess = true,
                                isFailed = false,
                                isLoading = false,
                                errorMessage = null
                            )
                        },
                    )
                }
            }
        }
    }
}