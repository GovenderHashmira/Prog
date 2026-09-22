package prog7314.poe.edubridge.data.model
data class Settings(
    val userId: String,
    val language: String = "en",
    val notificationEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val darkModeEnabled: Boolean = false,
    val weatherCity: String = "Johannesburg",
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isCustomised: Boolean
        get() = language != "en" ||
                !notificationEnabled ||
                biometricEnabled ||
                darkModeEnabled ||
                weatherCity != "Johannesburg"

    companion object {
        /** Default settings used before the first API/DataStore sync. */
        fun default(userId: String): Settings = Settings(userId = userId)

        /** Languages supported by the app (FR-17). */
        val SUPPORTED_LANGUAGES: List<LanguageOption> = listOf(
            LanguageOption("en", "English"),
            LanguageOption("af", "Afrikaans"),
            LanguageOption("zu", "isiZulu")
        )
    }
}

/** APP LANGUAGE **/
data class LanguageOption(
    val code: String,
    val displayName: String
)
