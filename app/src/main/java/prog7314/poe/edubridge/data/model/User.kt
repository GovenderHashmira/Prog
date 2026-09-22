package prog7314.poe.edubridge.data.model

import prog7314.poe.edubridge.data.Role

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: Role,
    val language: String = "en"
)