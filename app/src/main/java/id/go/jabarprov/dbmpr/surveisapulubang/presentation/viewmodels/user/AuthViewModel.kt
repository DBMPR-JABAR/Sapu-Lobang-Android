package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store.AuthAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store.AuthStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val store: AuthStore) : ViewModel() {

    val uiState = store.state.asStateFlow()

    private val actionChannel = Channel<AuthAction>()

    init {
        subscribeActionChannel()
    }

    fun processAction(action: AuthAction) {
        viewModelScope.launch {
            actionChannel.send(action)
        }
    }

    private fun subscribeActionChannel() {
        viewModelScope.launch {
            actionChannel
                .receiveAsFlow()
                .filterNotNull()
                .collect {
                    store.reduce(it)
                }
        }
    }

    override fun onCleared() {
        store.cancel()
        super.onCleared()
    }
}