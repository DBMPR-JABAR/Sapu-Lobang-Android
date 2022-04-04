package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.WithStore
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store.RekapitulasiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store.RekapitulasiState
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.store.RekapitulasiStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RekapitulasiViewModel @Inject constructor(private val rekapitulasiStore: RekapitulasiStore) :
    ViewModel(), WithStore<RekapitulasiAction, RekapitulasiState> {

    override val actionChannel: Channel<RekapitulasiAction> = Channel()

    override val uiState: StateFlow<RekapitulasiState> = rekapitulasiStore.state.asStateFlow()

    init {
        subscribeActionChannel()
    }

    override fun processAction(action: RekapitulasiAction) {
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
                    rekapitulasiStore.reduce(it)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        rekapitulasiStore.cancel()
    }
}
