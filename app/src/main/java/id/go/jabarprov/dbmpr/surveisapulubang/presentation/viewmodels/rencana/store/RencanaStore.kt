package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetListLubangPerencanaan
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class RencanaStore @Inject constructor(private val getListLubangPerencanaan: GetListLubangPerencanaan) :
    Store<RencanaAction, RencanaState>(RencanaState()) {
    override fun reduce(action: RencanaAction) {
        coroutineScope.launch {
            when (action) {
                is RencanaAction.UpdateRuasJalan -> {
                    state.value = state.value.copy(
                        ruasJalan = action.ruasJalan,
                        idRuasJalan = getIdRuasJalan(action.ruasJalan)
                    )
                }
                is RencanaAction.UpdateTanggal -> {
                    state.value = state.value.copy(
                        tanggal = CalendarUtils.formatTimeInMilliesToCalendar(action.timeMilles)
                    )
                }
                RencanaAction.LoadData -> {
                    state.value = state.value.copy(
                        isLoading = true,
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false
                    )
                    val currentState = state.value
                    val params = GetListLubangPerencanaan.Params(
                        currentState.tanggal,
                        currentState.idRuasJalan,
                    )
                    val result = getListLubangPerencanaan.run(params)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isLoading = false,
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false
                            )
                        },
                        fnR = { listLubang ->
                            state.value = state.value.copy(
                                isLoading = false,
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true,
                                listLubang = listLubang
                            )
                        }
                    )
                }
            }
        }
    }

    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}