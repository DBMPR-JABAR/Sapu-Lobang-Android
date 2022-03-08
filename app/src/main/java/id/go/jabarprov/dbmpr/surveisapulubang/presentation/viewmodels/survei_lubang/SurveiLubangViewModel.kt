package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.WithStore
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangState
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store.SurveiLubangStore
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveiLubangViewModel @Inject constructor(private val store: SurveiLubangStore) :
    ViewModel(), WithStore<SurveiLubangAction, SurveiLubangState> {

    override val uiState = store.state.asStateFlow()

    override val actionChannel = Channel<SurveiLubangAction>()

    init {
        subscribeActionChannel()
    }

    override fun processAction(action: SurveiLubangAction) {
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