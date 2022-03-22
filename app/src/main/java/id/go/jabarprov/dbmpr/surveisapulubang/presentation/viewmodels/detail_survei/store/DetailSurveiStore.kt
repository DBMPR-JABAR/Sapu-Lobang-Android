package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.detail_survei.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.DeleteSurveiItem
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetResultDetailSurvei
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailSurveiStore @Inject constructor(
    private val getResultDetailSurvei: GetResultDetailSurvei,
    private val deleteSurveiItem: DeleteSurveiItem
) :
    Store<DetailSurveiAction, DetailSurveiState>(DetailSurveiState()) {

    override fun reduce(action: DetailSurveiAction) {
        coroutineScope.launch {
            when (action) {
                is DetailSurveiAction.LoadSurveiData -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true,
                        isDelete = false,
                    )
                    val params = GetResultDetailSurvei.Params(action.tanggal, action.idRuasJalan)
                    val result = getResultDetailSurvei.run(params)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false,
                            )
                        },
                        fnR = { listLubang ->
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                listLubang = listLubang
                            )
                        },
                    )
                }
                is DetailSurveiAction.DeleteLubang -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val result = deleteSurveiItem.run(action.idLubang)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
                            )
                        },
                        fnR = {
                            state.value = state.value.copy(
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                isLoading = false,
                                isDelete = true
                            )
                        },
                    )
                }
            }
        }
    }

}