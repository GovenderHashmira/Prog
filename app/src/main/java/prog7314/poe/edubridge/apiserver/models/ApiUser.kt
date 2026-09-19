package prog7314.poe.edubridge.apiserver.models

// Internal user record for mock authentication.

data class ApiUser (
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val linkedStudentIds: List<String> = emptyList()
)
