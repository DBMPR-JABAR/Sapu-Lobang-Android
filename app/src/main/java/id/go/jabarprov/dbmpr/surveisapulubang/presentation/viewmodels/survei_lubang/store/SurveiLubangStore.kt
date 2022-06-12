package id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.survei_lubang.store

import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.core.store.Store
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.KategoriLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.KurangLubang
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.StartSurvei
import id.go.jabarprov.dbmpr.surveisapulubang.domain.usecases.TambahLubang
import id.go.jabarprov.dbmpr.surveisapulubang.utils.CalendarUtils
import kotlinx.coroutines.launch
import javax.inject.Inject

class SurveiLubangStore @Inject constructor(
    private val startSurvei: StartSurvei,
    private val tambahLubang: TambahLubang,
    private val kurangLubang: KurangLubang
) : Store<SurveiLubangAction, SurveiLubangState>(SurveiLubangState()) {
    override fun reduce(action: SurveiLubangAction) {
        coroutineScope.launch {
            when (action) {
                is SurveiLubangAction.StartSurveiAction -> startSurvei(action)
                is SurveiLubangAction.TambahLubangAction -> tambahLubang(action)
                is SurveiLubangAction.KurangLubangAction -> kurangLubang(action)
                is SurveiLubangAction.UpdateRuasJalan -> updateRuasJalan(action)
                is SurveiLubangAction.UpdateTanggal -> updateTanggal(action)
                is SurveiLubangAction.UpdateKodeLokasi -> updateKodeLokasi(action)
                is SurveiLubangAction.UpdateLokasiKm -> updateLokasiKm(action)
                is SurveiLubangAction.UpdateLokasiM -> updateLokasiM(action)
                is SurveiLubangAction.UpdateFotoLubang -> updateFotoLubang(action)
                is SurveiLubangAction.UpdateKategoriLubang -> updateKategoriLubang(action)
                is SurveiLubangAction.UpdatePanjangLubang -> updatePanjangLubang(action)
                is SurveiLubangAction.UpdateJumlahLubangPerGroup -> updateJumlahLubangPerGroup(
                    action
                )
                is SurveiLubangAction.UpdateKeterangan -> updateKeterangan(action)
                SurveiLubangAction.ResetStartState -> resetStartState()
                SurveiLubangAction.ResetTambahState -> resetTambahState()
                SurveiLubangAction.ResetKurangState -> resetKurangState()
                is SurveiLubangAction.UpdateKedalaman -> updateKedalaman(action)
                is SurveiLubangAction.UpdateLajur -> updateLajur(action)
                is SurveiLubangAction.UpdateUkuran -> updateUkuran(action)
                is SurveiLubangAction.UpdatePotensiLubang -> updatePotensi(action)
                is SurveiLubangAction.GetLocation -> updateGetLocation()
                is SurveiLubangAction.GetLocationFailed -> updateGetLocationFailed(action)
            }
        }
    }

    private fun updateRuasJalan(action: SurveiLubangAction.UpdateRuasJalan) {
        state.value = state.value.copy(
            idRuasJalan = getIdRuasJalan(action.ruasJalan),
            ruasJalan = action.ruasJalan
        )
    }

    private fun updateTanggal(action: SurveiLubangAction.UpdateTanggal) {
        state.value = state.value.copy(
            tanggal = CalendarUtils.formatTimeInMilliesToCalendar(action.timeInMillis)
        )
    }

    private fun updateKodeLokasi(action: SurveiLubangAction.UpdateKodeLokasi) {
        state.value = state.value.copy(
            kodeLokasi = action.kodeLokasi
        )
    }

    private fun updateLokasiKm(action: SurveiLubangAction.UpdateLokasiKm) {
        state.value = state.value.copy(
            lokasiKm = action.lokasiKm
        )
    }

    private fun updateLokasiM(action: SurveiLubangAction.UpdateLokasiM) {
        state.value = state.value.copy(
            lokasiM = action.lokasiM
        )
    }

    private fun updateFotoLubang(action: SurveiLubangAction.UpdateFotoLubang) {
        state.value = state.value.copy(
            gambarLubangUri = action.gambarLubang,
            gambarLubangFile = action.gambarLubangFile
        )
    }

    private fun updateKategoriLubang(action: SurveiLubangAction.UpdateKategoriLubang) {
        state.value = state.value.copy(
            kategoriLubang = action.kategoriLubang
        )
    }

    private fun updatePanjangLubang(action: SurveiLubangAction.UpdatePanjangLubang) {
        state.value = state.value.copy(
            panjangLubang = action.panjangLubang
        )
    }

    private fun updateJumlahLubangPerGroup(action: SurveiLubangAction.UpdateJumlahLubangPerGroup) {
        state.value = state.value.copy(
            jumlahLubangPerGroup = action.jumlahLubangPerGroup
        )
    }

    private fun updateKeterangan(action: SurveiLubangAction.UpdateKeterangan) {
        state.value = state.value.copy(
            keteranganLubang = action.keterangan
        )
    }

    private fun updateKedalaman(action: SurveiLubangAction.UpdateKedalaman) {
        state.value = state.value.copy(
            kedalaman = action.kedalaman
        )
    }

    private fun updateLajur(action: SurveiLubangAction.UpdateLajur) {
        state.value = state.value.copy(
            lajur = action.lajur
        )
    }

    private fun updateUkuran(action: SurveiLubangAction.UpdateUkuran) {
        state.value = state.value.copy(
            ukuran = action.ukuran
        )
    }

    private fun updatePotensi(action: SurveiLubangAction.UpdatePotensiLubang) {
        state.value = state.value.copy(
            isPotential = action.isPotential
        )
    }

    private fun updateGetLocation() {
        state.value = state.value.copy(
            location = Resource.Loading()
        )
    }

    private fun updateGetLocationFailed(action: SurveiLubangAction.GetLocationFailed) {
        state.value = state.value.copy(
            location = Resource.Failed(action.message)
        )
    }

    private fun resetStartState() {
        state.value = state.value.copy(
            startSurveiLubang = Resource.Initial()
        )
    }

    private fun resetTambahState() {
        state.value = state.value.copy(
            tambahLubang = Resource.Initial()
        )
    }

    private fun resetKurangState() {
        state.value = state.value.copy(
            kurangLubang = Resource.Initial()
        )
    }

    private suspend fun startSurvei(action: SurveiLubangAction.StartSurveiAction) {
        state.value = state.value.copy(
            startSurveiLubang = Resource.Loading()
        )
        val param = StartSurvei.Params(action.tanggal, action.idRuasJalan)
        val result = startSurvei.run(param)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    startSurveiLubang = Resource.Failed(failure.message)
                )
            },
            fnR = { surveiLubang ->
                state.value = state.value.copy(
                    startSurveiLubang = Resource.Success(surveiLubang),
                    isStarted = true
                )
            }
        )
    }

    private suspend fun tambahLubang(action: SurveiLubangAction.TambahLubangAction) {
        state.value = state.value.copy(
            tambahLubang = Resource.Loading()
        )
        val param = TambahLubang.Params(
            tanggal = state.value.tanggal,
            idRuasJalan = state.value.idRuasJalan,
            kodeLokasi = state.value.kodeLokasi,
            lokasiKm = state.value.lokasiKm,
            lokasiM = state.value.lokasiM,
            lat = action.lat,
            long = action.long,
            panjangLubang = state.value.panjangLubang,
            jumlahLubangPerGroup = state.value.jumlahLubangPerGroup,
            kategoriLubang = if (state.value.kategoriLubang == KategoriLubang.SINGLE) "Single" else "Group",
            gambarLubang = state.value.gambarLubangFile!!,
            keterangan = state.value.keteranganLubang,
            lajur = state.value.lajur!!,
            ukuran = state.value.ukuran!!,
            kedalaman = state.value.kedalaman!!,
            isPotential = state.value.isPotential,
            onProgressUpdate = action.onProgressUpdate
        )
        val result = tambahLubang.run(param)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    tambahLubang = Resource.Failed(failure.message)
                )
            },
            fnR = { surveiLubang ->
                state.value = state.value.copy(
                    tambahLubang = Resource.Success(surveiLubang),
                    gambarLubangFile = null,
                    gambarLubangUri = null,
                    keteranganLubang = null,
                    panjangLubang = 0.0,
                    jumlahLubangPerGroup = 0,
                    isPotential = false,
                    lajur = null
                )
            }
        )
    }

    private suspend fun kurangLubang(action: SurveiLubangAction.KurangLubangAction) {
        state.value = state.value.copy(
            kurangLubang = Resource.Loading()
        )
        val param = KurangLubang.Params(
            state.value.tanggal,
            state.value.idRuasJalan,
            state.value.kodeLokasi,
            state.value.lokasiKm,
            state.value.lokasiM,
            action.lat,
            action.long
        )
        val result = kurangLubang.run(param)
        result.either(
            fnL = { failure ->
                state.value = state.value.copy(
                    kurangLubang = Resource.Failed(failure.message)
                )
            },
            fnR = { surveiLubang ->
                state.value = state.value.copy(
                    kurangLubang = Resource.Success(surveiLubang)
                )
            }
        )
    }

    private fun getIdRuasJalan(ruasJalan: String): String {
        return ruasJalan.split(" - ").last()
    }
}