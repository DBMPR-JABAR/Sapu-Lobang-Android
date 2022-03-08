package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.WithStore
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store.PenangananAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store.PenangananState
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store.PenangananStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PenangananViewModel @Inject constructor(private val store: PenangananStore) : ViewModel(),
    WithStore<PenangananAction, PenangananState> {

    override val actionChannel: Channel<PenangananAction> = Channel()

    override val uiState: StateFlow<PenangananState> = store.state.asStateFlow()

    init {
        subscribeActionChannel()
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

    override fun processAction(action: PenangananAction) {
        viewModelScope.launch {
            actionChannel.send(action)
        }
    }

    override fun onCleared() {
        store.cancel()
        super.onCleared()
    }
}