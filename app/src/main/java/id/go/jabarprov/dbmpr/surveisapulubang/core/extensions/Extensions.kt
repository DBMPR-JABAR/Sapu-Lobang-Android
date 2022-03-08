package id.go.jabarprov.dbmpr.surveisapulubang.core.extensions

import id.go.jabarprov.dbmpr.surveisapulubang.core.either.Either

fun <R> R.toSuccess(): Either.Success<R> {
    return Either.Success(this)
}

fun <L> L.toError(): Either.Error<L> {
    return Either.Error(this)
}