package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/schools/{id} — response body. Used by the Google Maps screen. */
data class SchoolDto(
    @SerializedName("schoolId")  val schoolId: String,
    @SerializedName("name")      val name: String,
    @SerializedName("address")   val address: String,
    @SerializedName("latitude")  val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("phone")     val phone: String? = null,
    @SerializedName("email")     val email: String? = null
)