package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class PenangananStore @Inject constructor() :
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
                is PenangananAction.UpdateKodeLokasi -> {
                    state.value = state.value.copy(
                        kodeLokasi = action.kodeLokasi
                    )
                }
                is PenangananAction.UpdateLokasiKm -> {
                    state.value = state.value.copy(
                        lokasiKm = action.lokasiKm
                    )
                }
                is PenangananAction.UpdateLokasiM -> {
                    state.value = state.value.copy(
                        lokasiM = action.lokasiM
                    )
                }
                is PenangananAction.TambahLubang -> {

                }
                is PenangananAction.KurangLubang -> {

                }
            }
        }
    }

    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}