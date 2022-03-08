package id.go.jabarprov.dbmpr.surveisapulubang.data.mapper

import id.go.jabarprov.dbmpr.surveisapulubang.data.models.response.RuasResponse
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Ruas

abstract class RuasDataMapper {
    companion object {
        fun convertRuasDataResponseToEntity(ruasResponse: RuasResponse): Ruas {
            return Ruas(id = ruasResponse.idRuasJalan, namaRuas = ruasResponse.namaRuasJalan)
        }

        fun convertListOfRuasDataResponseToListOfEntity(listRuasResponse: List<RuasResponse>): List<Ruas> {
            return listRuasResponse.map { convertRuasDataResponseToEntity(it) }
        }
    }
}