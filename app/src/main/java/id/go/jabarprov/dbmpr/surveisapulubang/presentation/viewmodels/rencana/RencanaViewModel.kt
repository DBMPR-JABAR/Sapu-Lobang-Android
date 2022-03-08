package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.WithStore
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store.RencanaAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store.RencanaState
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store.RencanaStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RencanaViewModel @Inject constructor(private val store: RencanaStore) : ViewModel(),
    WithStore<RencanaAction, RencanaState> {

    override val actionChannel: Channel<RencanaAction> = Channel()

    override val uiState: StateFlow<RencanaState> = store.state.asStateFlow()

    init {
        subscribeActionChannel()
    }

    override fun processAction(action: RencanaAction) {
        viewModelScope.launch {
            actionChannel.send(action)
        }
    }

    override fun subscribeActionChannel() {
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
        super.onCleared()
        store.cancel()
    }
}