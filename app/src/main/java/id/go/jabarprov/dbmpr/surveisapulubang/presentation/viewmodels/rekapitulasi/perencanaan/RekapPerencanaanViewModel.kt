package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.WithStore
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.store.RekapPerencanaanAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.store.RekapPerencanaanState
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rekapitulasi.perencanaan.store.RekapPerencanaanStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RekapPerencanaanViewModel @Inject constructor(private val store: RekapPerencanaanStore) :
    ViewModel(), WithStore<RekapPerencanaanAction, RekapPerencanaanState> {

    override val actionChannel: Channel<RekapPerencanaanAction> = Channel()

    override val uiState: StateFlow<RekapPerencanaanState> = store.state

    init {
        subscribeActionChannel()
    }

    override fun processAction(action: RekapPerencanaanAction) {
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