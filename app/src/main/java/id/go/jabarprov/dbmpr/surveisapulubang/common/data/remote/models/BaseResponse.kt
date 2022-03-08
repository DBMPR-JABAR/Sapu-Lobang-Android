package id.go.jabarprov.dbmpr.surveisapulubang.common.data.remote.models

data class BaseResponse<T>(
    val status: String?,
    val success: Boolean?,
    val message: String?,
    val data: T
)
