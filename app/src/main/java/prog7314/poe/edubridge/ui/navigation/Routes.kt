package prog7314.poe.edubridge.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val BIOMETRIC = "biometric"
    const val DASHBOARD = "dashboard"
    const val STUDENTS = "students"
    const val RESULTS = "results"
    const val ATTENDANCE = "attendance"
    const val TIMETABLE = "timetable"
    const val NOTICES = "notices"
    const val MESSAGES = "messages"
    const val MESSAGE_DETAIL = "message_detail"
    const val SETTINGS = "settings"
    const val LANGUAGE = "language"
    const val SYNC = "sync"
    const val PROFILE = "profile"
    const val MAPS = "maps"

    const val ARG_MESSAGE_ID = "messageId"
    const val MESSAGE_DETAIL_PATTERN = "$MESSAGE_DETAIL/{$ARG_MESSAGE_ID}"

    fun messageDetail(messageId: String): String = "$MESSAGE_DETAIL/$messageId"
}
