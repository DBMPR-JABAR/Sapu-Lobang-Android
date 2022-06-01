package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.rencana.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.GetListLubangPerencanaan
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.RejectLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.UploadRencanaPenangananLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class RencanaStore @Inject constructor(
    private val getListLubangPerencanaan: GetListLubangPerencanaan,
    private val uploadRencanaPenangananLubang: UploadRencanaPenangananLubang,
    private val rejectLubang: RejectLubang
) :
    Store<RencanaAction, RencanaState>(RencanaState()) {
    override fun reduce(action: RencanaAction) {
        coroutineScope.launch {
            when (action) {
                is RencanaAction.UpdateRuasJalan -> updateRuasJalan(action)
                is RencanaAction.UpdateTanggal -> updateTanggal(action)
                RencanaAction.GetListLubang -> loadLubangPerencanaan()
                is RencanaAction.UploadRencanaLubang -> uploadRencanaLubang(action)
                is RencanaAction.RejectLubang -> rejectLubang(action)
            }
        }
    }

    private fun updateRuasJalan(action: RencanaAction.UpdateRuasJalan) {
        state.value = state.value.copy(
            ruasJalan = action.ruasJalan,
            idRuasJalan = getIdRuasJalan(action.ruasJalan)
        )
    }

    private fun updateTanggal(action: RencanaAction.UpdateTanggal) {
        state.value = state.value.copy(
            tanggal = CalendarUtils.formatTimeInMilliesToCalendar(action.timeMilles)
        )
    }

    private suspend fun loadLubangPerencanaan() {
        state.value = state.value.copy(
            listLubang = Resource.Loading(),
            rejectLubang = Resource.Initial()
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
                    listLubang = Resource.Failed(failure.message)
                )
            },
            fnR = { listLubang ->
                state.value = state.value.copy(
                    listLubang = Resource.Success(listLubang)
                )
            }
        )
    }

    private suspend fun uploadRencanaLubang(action: RencanaAction.UploadRencanaLubang) {
        state.value = state.value.copy(
            listLubang = Resource.Loading()
        )
        val params = UploadRencanaPenangananLubang.Params(
            action.idLubang,
            state.value.tanggal,
            action.keterangan
        )
        val result = uploadRencanaPenangananLubang.run(params)
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
            }
        )
    }

    private suspend fun rejectLubang(action: RencanaAction.RejectLubang) {
        state.value = state.value.copy(
            rejectLubang = Resource.Loading()
        )
        val params = RejectLubang.Params(action.idLubang)
        val result = rejectLubang.run(params)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    rejectLubang = Resource.Failed(failure.message)
                )
            },
            fnR = {
                state.value = state.value.copy(
                    rejectLubang = Resource.Success(Unit)
                )
            }
        )
    }


    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}