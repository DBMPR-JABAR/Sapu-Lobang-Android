package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetListLubangPenanganan
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.UploadPenangananLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PenangananStore"

class PenangananStore @Inject constructor(
    private val getListLubangPenanganan: GetListLubangPenanganan,
    private val uploadPenangananLubang: UploadPenangananLubang
) :
    Store<PenangananAction, PenangananState>(PenangananState()) {
    override fun reduce(action: PenangananAction) {
        coroutineScope.launch {
            when (action) {
                is PenangananAction.UpdateRuasJalan -> {
                    state.value = state.value.copy(
                        ruasJalan = action.ruasJalan,
                        idRuasJalan = getIdRuasJalan(action.ruasJalan)
                    )
                }
                is PenangananAction.UpdateTanggal -> {
                    state.value = state.value.copy(
                        tanggal = CalendarUtils.formatTimeInMilliesToCalendar(action.timeMilles)
                    )
                }
                is PenangananAction.GetListLubang -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val param =
                        GetListLubangPenanganan.Params(state.value.tanggal, state.value.idRuasJalan)
                    val result = getListLubangPenanganan.run(param)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
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
                is PenangananAction.StorePenangananLubang -> {
                    state.value = state.value.copy(
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false,
                        isLoading = true
                    )
                    val param =
                        UploadPenangananLubang.Params(
                            action.idLubang,
                            state.value.tanggal,
                            action.keterangan,
                            action.gambarPenanganan,
                            action.latitude,
                            action.longitude
                        )
                    val result = uploadPenangananLubang.run(param)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false,
                                isLoading = false
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
            }
        }
    }

    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}