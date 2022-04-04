package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RekapitulasiResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi

abstract class RekapitulasiDataMapper {
    companion object {
        fun convertRekapitulasiResponseToEntity(rekapitulasiResponse: RekapitulasiResponse): Rekapitulasi {
            return Rekapitulasi(
                totalDataSurvei = rekapitulasiResponse.data_survei,
                totalPerencanaan = rekapitulasiResponse.perencanaan,
                totalPenanganan = rekapitulasiResponse.penanganan
            )
        }
    }
}