package id.go.jabarprov.dbmpr.surveisapulubang.domain.entities

import java.util.*

data class User(
    val id: Int,
    val nama: String,
    val email: String,
    val role: String,
    val idInternalRole: Int,
    val internalRole: Role,
    val sup: String?,
    val idSup: Int?,
    val uptd: String?,
    val idUptd: Int?,
    val ruasJalan: List<Ruas>,
    val token: String,
    val tokenExpiredDate: Calendar
)

enum class Role {
    ADMINISTRATOR,
    KSUP,
    MANDOR,
    UNSUPPORTED;

    fun convertToString(): String {
        return when (this) {
            ADMINISTRATOR -> "Administrator"
            KSUP -> "Kepala Satuan Unit Pemeliharaan"
            MANDOR -> "Mandor"
            UNSUPPORTED -> "Unsupported"
        }
    }

    companion object {
        fun convertStringToRole(role: String): Role {
            return when (role) {
                "Administrator" -> ADMINISTRATOR
                "Kepala Satuan Unit Pemeliharaan" -> KSUP
                "Mandor" -> MANDOR
                else -> UNSUPPORTED
            }
        }
    }
}
