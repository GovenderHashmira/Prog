package prog7314.poe.edubridge.apiserver.models

data class ApiNotice(
    val id: String,
    val title: String,
    val body: String,
    val date: String,
    val audienceRole: String = "Parent"
)