package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.WithStore
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store.DetailSurveiAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store.DetailSurveiState
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store.DetailSurveiStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailSurveiViewModel @Inject constructor(private val store: DetailSurveiStore) : ViewModel(),
    WithStore<DetailSurveiAction, DetailSurveiState> {

    override val actionChannel: Channel<DetailSurveiAction> = Channel()

    override val uiState: StateFlow<DetailSurveiState> = store.state.asStateFlow()

    init {
        subscribeActionChannel()
    }

    override fun processAction(action: DetailSurveiAction) {
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
        store.cancel()
        super.onCleared()
    }
}