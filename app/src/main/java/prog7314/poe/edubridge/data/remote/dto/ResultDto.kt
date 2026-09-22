package prog7314.poe.edubridge.data.remote.dto

import com.google.gson.annotations.SerializedName

/** GET /api/students/{id}/results — response element. */
data class ResultDto(
    @SerializedName("resultId")       val resultId: String,
    @SerializedName("studentId")      val studentId: String,
    @SerializedName("subjectId")      val subjectId: String,
    @SerializedName("subjectName")    val subjectName: String,
    @SerializedName("assessmentName") val assessmentName: String,
    @SerializedName("score")          val score: Double,
    @SerializedName("academicPeriod") val academicPeriod: String,
    @SerializedName("updatedAt")      val updatedAt: String        // ISO-8601
)