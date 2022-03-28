package id.go.jabarprov.dbmpr.surveisapulubang.domain.entities

import java.util.*

data class User(
    val id: Int,
    val nama: String,
    val email: String,
    val role: String,
    val idInternalRole: Int,
    val internalRole: String,
    val sup: String?,
    val idSup: Int?,
    val uptd: String?,
    val idUptd: Int?,
    val ruasJalan: List<Ruas>,
    val token: String,
    val tokenExpiredDate: Calendar
)
