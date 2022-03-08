package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("email_verified_at") val emailVerifiedAt: String,
    val role: String,
    @SerializedName("internal_role_id") val internalRoleId: Int,
    val sup: String,
    @SerializedName("sup_id") val supId: Int,
    @SerializedName("uptd_id") val uptdId: Int,
    val blokir: String,
    @SerializedName("is_delete") val isDeleted: String?,
    val ruas: List<RuasResponse>,
    @SerializedName("encrypted_id") val encryptedId: String,
    @SerializedName("internal_role") val internalRole: RoleResponse
)