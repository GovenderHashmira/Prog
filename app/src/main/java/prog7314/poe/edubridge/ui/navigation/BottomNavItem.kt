package prog7314.poe.edubridge.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import prog7314.poe.edubridge.R

enum class BottomNavItem(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector
) {
    HOME(Routes.DASHBOARD, R.string.nav_home, Icons.Filled.Home),
    ACADEMICS(Routes.RESULTS, R.string.nav_academics, Icons.Filled.MenuBook),
    TIMETABLE(Routes.TIMETABLE, R.string.nav_timetable, Icons.Filled.CalendarMonth),
    MESSAGES(Routes.MESSAGES, R.string.nav_messages, Icons.AutoMirrored.Filled.Chat),
    SETTINGS(Routes.SETTINGS, R.string.nav_settings, Icons.Filled.Settings)
}
