package prog7314.poe.edubridge.apiserver.models

data class ApiStudent(
    val id: String,
    val name: String,
    val grade: String,
    val schoolId: String,
    val studentNumber: String,
    val active: Boolean = true
)