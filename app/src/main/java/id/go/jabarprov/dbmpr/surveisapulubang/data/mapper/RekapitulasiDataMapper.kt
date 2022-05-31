package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RekapitulasiResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Rekapitulasi

abstract class RekapitulasiDataMapper {
    companion object {
        fun convertRekapitulasiResponseToEntity(rekapitulasiResponse: RekapitulasiResponse): Rekapitulasi {
            return Rekapitulasi(
                jumlah = convertRekapitulasiJumlahResponseToEntity(rekapitulasiResponse.jumlah),
                panjang = convertRekapitulasiPanjangResponseToEntity(rekapitulasiResponse.panjang)
            )
        }

        fun convertRekapitulasiJumlahResponseToEntity(rekapJumlahResponse: RekapitulasiResponse.Jumlah): Rekapitulasi.Jumlah {
            return Rekapitulasi.Jumlah(
                sisa = rekapJumlahResponse.sisa,
                perencanaan = rekapJumlahResponse.perencanaan,
                penanganan = rekapJumlahResponse.penanganan,
                potensi = rekapJumlahResponse.potensi
            )
        }

        fun convertRekapitulasiPanjangResponseToEntity(rekapPanjangResponse: RekapitulasiResponse.Panjang): Rekapitulasi.Panjang {
            return Rekapitulasi.Panjang(
                sisa = rekapPanjangResponse.sisa,
                perencanaan = rekapPanjangResponse.perencanaan,
                penanganan = rekapPanjangResponse.penanganan,
                potensi = rekapPanjangResponse.potensi
            )
        }
    }
}