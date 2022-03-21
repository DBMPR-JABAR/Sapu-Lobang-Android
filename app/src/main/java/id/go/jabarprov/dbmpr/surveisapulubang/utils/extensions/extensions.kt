package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

fun <T> T?.getValueOrElse(defaultValue: T): T {
    return when {
        this == null -> defaultValue
        else -> {
            this
        }
    }
}