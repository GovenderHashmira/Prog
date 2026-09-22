package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/students — response element. */
data class StudentDto(
    @SerializedName("studentId")     val studentId: String,
    @SerializedName("userId")        val userId: String?,
    @SerializedName("schoolId")      val schoolId: String,
    @SerializedName("gradeId")       val gradeId: String,
    @SerializedName("firstName")     val firstName: String,
    @SerializedName("lastName")      val lastName: String,
    @SerializedName("studentNumber") val studentNumber: String,
    @SerializedName("active")        val active: Boolean = true,
    @SerializedName("schoolName")    val schoolName: String? = null,
    @SerializedName("gradeName")     val gradeName: String? = null
)