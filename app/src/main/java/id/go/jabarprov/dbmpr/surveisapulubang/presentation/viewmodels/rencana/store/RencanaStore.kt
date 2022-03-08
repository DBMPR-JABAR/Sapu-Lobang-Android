package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.UploadRencanaPenangananLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class RencanaStore @Inject constructor(private val uploadRencanaPenangananLubang: UploadRencanaPenangananLubang) :
    Store<RencanaAction, RencanaState>(RencanaState()) {
    override fun reduce(action: RencanaAction) {
        coroutineScope.launch {
            when (action) {
                is RencanaAction.UpdateJumlahRencanaPenanganan -> {
                    state.value = state.value.copy(jumlahPenanganan = action.jumlah)
                }
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
                RencanaAction.UploadRencanaPenanganan -> {
                    state.value = state.value.copy(
                        isLoading = true,
                        isFailed = false,
                        errorMessage = "",
                        isSuccess = false
                    )
                    val currentState = state.value
                    val params = UploadRencanaPenangananLubang.Params(
                        currentState.tanggal,
                        currentState.idRuasJalan,
                        currentState.jumlahPenanganan
                    )
                    val result = uploadRencanaPenangananLubang.run(params)
                    result.either(
                        fnL = { failure ->
                            state.value = state.value.copy(
                                isLoading = false,
                                isFailed = true,
                                errorMessage = failure.message,
                                isSuccess = false
                            )
                        },
                        fnR = {
                            state.value = state.value.copy(
                                isLoading = false,
                                isFailed = false,
                                errorMessage = "",
                                isSuccess = true
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