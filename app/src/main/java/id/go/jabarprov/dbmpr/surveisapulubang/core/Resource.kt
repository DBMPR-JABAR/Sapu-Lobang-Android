package id.go.jabarprov.dbmpr.surveisapulubang.core

sealed class Resource<T> {
    class Initial<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Failed<T>(val errorMessage: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}