package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.WithStore
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.store.RekapPenangananAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.store.RekapPenangananState
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.penanganan.store.RekapPenangananStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RekapPenangananViewModel @Inject constructor(private val store: RekapPenangananStore) :
    ViewModel(),
    WithStore<RekapPenangananAction, RekapPenangananState> {

    override val actionChannel: Channel<RekapPenangananAction> = Channel()

    override val uiState: StateFlow<RekapPenangananState> = store.state

    init {
        subscribeActionChannel()
    }

    override fun processAction(action: RekapPenangananAction) {
        viewModelScope.launch {
            actionChannel.send(action)
        }
    }

    override fun subscribeActionChannel() {
        viewModelScope.launch {
            actionChannel.receiveAsFlow()
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