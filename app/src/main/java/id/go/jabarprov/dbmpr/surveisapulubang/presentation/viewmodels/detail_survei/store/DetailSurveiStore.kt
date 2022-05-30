package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.None
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.DeleteSurveiItem
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.DeleteSurveiPotensiItem
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetResultSurvei
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailSurveiStore @Inject constructor(
    private val getResultSurvei: GetResultSurvei,
    private val deleteSurveiItem: DeleteSurveiItem,
    private val deleteSurveiPotensiItem: DeleteSurveiPotensiItem
) :
    Store<DetailSurveiAction, DetailSurveiState>(DetailSurveiState()) {

    override fun reduce(action: DetailSurveiAction) {
        coroutineScope.launch {
            when (action) {
                is DetailSurveiAction.LoadSurveiData -> loadSurvei(action)
                is DetailSurveiAction.DeleteLubang -> deleteLubang(action)
                is DetailSurveiAction.DeletePotensiLubang -> deletePotensiLubang(action)
            }
        }
    }

    private suspend fun loadSurvei(action: DetailSurveiAction.LoadSurveiData) {
        state.value = state.value.copy(
            resultSurveiState = Resource.Loading(),
            deleteItemState = Resource.Initial()
        )
        val params = GetResultSurvei.Params(action.tanggal, action.idRuasJalan)
        val result = getResultSurvei.run(params)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    resultSurveiState = Resource.Failed(failure.message)
                )
            },
            fnR = { resultLubang ->
                state.value = state.value.copy(
                    resultSurveiState = Resource.Success(resultLubang)
                )
            },
        )
    }

    private suspend fun deleteLubang(action: DetailSurveiAction.DeleteLubang) {
        state.value = state.value.copy(
            deleteItemState = Resource.Loading()
        )
        val result = deleteSurveiItem.run(action.idLubang)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    deleteItemState = Resource.Failed(failure.message)
                )
            },
            fnR = {
                state.value = state.value.copy(
                    deleteItemState = Resource.Success(None)
                )
            },
        )
    }

    private suspend fun deletePotensiLubang(action: DetailSurveiAction.DeletePotensiLubang) {
        state.value = state.value.copy(
            deleteItemState = Resource.Loading()
        )
        val result = deleteSurveiPotensiItem.run(action.idLubang)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    deleteItemState = Resource.Failed(failure.message)
                )
            },
            fnR = {
                state.value = state.value.copy(
                    deleteItemState = Resource.Success(None)
                )
            },
        )
    }

}