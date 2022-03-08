package id.go.jabarprov.dbmpr.surveisapulubang.data.models.response

import com.google.gson.annotations.SerializedName

data class RoleResponse(
    val id: Int,
    val role: String,
    val parent: Int,
    @SerializedName("is_superadmin") val isSuperAdmin: Int,
    val keterangan: String,
    @SerializedName("is_active") val isActive: Int,
    @SerializedName("is_deleted") val isDeleted: Int,
    val uptd: String,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("created_by") val createdBy: String?,
    @SerializedName("updated_by") val updatedBy: String?,
    @SerializedName("parent_id") val parentId: Int
)