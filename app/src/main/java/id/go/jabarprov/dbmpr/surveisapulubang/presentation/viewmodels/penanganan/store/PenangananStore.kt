package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.penanganan.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
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
                is PenangananAction.UpdateRuasJalan -> updateRuasJalan(action)
                is PenangananAction.UpdateTanggal -> updateTanggal(action)
                is PenangananAction.GetListLubang -> getListLubang()
                is PenangananAction.StorePenangananLubang -> storePenangananLubang(action)
                PenangananAction.GetLocation -> getLocation()
                is PenangananAction.GetLocationFailed -> getLocationFailed(action)
            }
        }
    }

    private fun updateRuasJalan(action: PenangananAction.UpdateRuasJalan) {
        state.value = state.value.copy(
            ruasJalan = action.ruasJalan,
            idRuasJalan = getIdRuasJalan(action.ruasJalan)
        )
    }

    private fun updateTanggal(action: PenangananAction.UpdateTanggal) {
        state.value = state.value.copy(
            tanggal = CalendarUtils.formatTimeInMilliesToCalendar(action.timeMilles)
        )
    }

    private suspend fun getListLubang() {
        state.value = state.value.copy(
            listLubang = Resource.Loading(),
            storePenanganan = Resource.Initial()
        )
        val param =
            GetListLubangPenanganan.Params(state.value.tanggal, state.value.idRuasJalan)
        val result = getListLubangPenanganan.run(param)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    listLubang = Resource.Failed(failure.message)
                )
            },
            fnR = { listLubang ->
                state.value = state.value.copy(
                    listLubang = Resource.Success(listLubang)
                )
            },
        )
    }

    private suspend fun storePenangananLubang(action: PenangananAction.StorePenangananLubang) {
        state.value = state.value.copy(
            storePenanganan = Resource.Loading(),
            location = Resource.Initial()
        )
        val param =
            UploadPenangananLubang.Params(
                action.idLubang,
                state.value.tanggal,
                action.keterangan,
                action.gambarPenanganan,
                action.latitude,
                action.longitude,
                action.onProgressUpdate
            )
        val result = uploadPenangananLubang.run(param)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    storePenanganan = Resource.Failed(failure.message)
                )
            },
            fnR = { listLubang ->
                state.value = state.value.copy(
                    storePenanganan = Resource.Success(listLubang)
                )
            },
        )
    }

    private fun getLocation() {
        state.value = state.value.copy(
            location = Resource.Loading()
        )
    }

    private fun getLocationFailed(action: PenangananAction.GetLocationFailed) {
        state.value = state.value.copy(
            location = Resource.Failed(action.message)
        )
    }

    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}