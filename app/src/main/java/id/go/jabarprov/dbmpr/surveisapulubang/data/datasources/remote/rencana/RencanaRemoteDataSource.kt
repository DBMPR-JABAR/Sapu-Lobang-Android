package id.go.jabarprov.dbmpr.surveisapulubang.data.datasources.remote.rencana

import java.util.*

interface RencanaRemoteDataSource {
    suspend fun storeRencana(tanggal: Calendar, idRuasJalan: String, jumlah: Int)
}