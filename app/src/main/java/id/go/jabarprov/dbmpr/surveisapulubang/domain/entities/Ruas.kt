package id.go.jabarprov.dbmpr.surveisapulubang.domain.entities

data class Ruas(
    val id: String,
    val namaRuas: String
) {
    fun getIdRuasJalan(): String {
        return namaRuas.split(" - ").last()
    }
}
