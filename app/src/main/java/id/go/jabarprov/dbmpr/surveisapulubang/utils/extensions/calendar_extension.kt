package id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions

import java.util.*

fun Calendar.addSeconds(seconds: Int) = this.apply { add(Calendar.SECOND, seconds) }