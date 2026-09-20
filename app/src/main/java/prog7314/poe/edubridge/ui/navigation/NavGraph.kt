package prog7314.poe.edubridge.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import prog7314.poe.edubridge.ui.components.BottomNavBar
import prog7314.poe.edubridge.ui.screens.attendance.AttendanceScreen
import prog7314.poe.edubridge.ui.screens.biometrics.BiometricScreen
import prog7314.poe.edubridge.ui.screens.dashboard.DashboardScreen
import prog7314.poe.edubridge.ui.screens.language.LanguageScreen
import prog7314.poe.edubridge.ui.screens.login.LoginScreen
import prog7314.poe.edubridge.ui.screens.maps.MapsScreen
import prog7314.poe.edubridge.ui.screens.messages.MessageDetailScreen
import prog7314.poe.edubridge.ui.screens.messages.MessagesScreen
import prog7314.poe.edubridge.ui.screens.notices.NoticesScreen
import prog7314.poe.edubridge.ui.screens.profile.ProfileScreen
import prog7314.poe.edubridge.ui.screens.results.ResultsScreen
import prog7314.poe.edubridge.ui.screens.settings.SettingsScreen
import prog7314.poe.edubridge.ui.screens.settings.SettingsViewModel
import prog7314.poe.edubridge.ui.screens.splash.SplashScreen
import prog7314.poe.edubridge.ui.screens.students.StudentSelectionScreen
import prog7314.poe.edubridge.ui.screens.sync.SyncStatusScreen
import prog7314.poe.edubridge.ui.screens.timetable.TimetableScreen
import prog7314.poe.edubridge.ui.session.SessionViewModel

@Composable
fun EduBridgeNavGraph(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val session by sessionViewModel.state.collectAsStateWithLifecycle()
    val settings by settingsViewModel.settings.collectAsStateWithLifecycle()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val mainRoutes = BottomNavItem.entries.map { it.route }.toSet()
    val showBottomBar = currentRoute in mainRoutes

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onItemSelected = { route ->
                        navController.navigate(route) {
                            popUpTo(Routes.DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    onAuthenticated = {
                        navController.navigate(Routes.BIOMETRIC) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.BIOMETRIC) {
                BiometricScreen(
                    userName = session.user.name,
                    onAuthenticated = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.BIOMETRIC) { inclusive = true }
                        }
                    },
                    onUseSso = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.BIOMETRIC) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.DASHBOARD) {
                DashboardScreen(
                    userName = session.user.name,
                    selectedStudent = session.selectedStudent,
                    weatherCity = settings.weatherCity,
                    onViewSchedule = {
                        navController.navigate(Routes.TIMETABLE) { launchSingleTop = true }
                    },
                    onSeeAllNotices = { navController.navigate(Routes.NOTICES) },
                    onNotifications = { navController.navigate(Routes.NOTICES) }
                )
            }

            composable(Routes.STUDENTS) {
                StudentSelectionScreen(
                    students = session.students,
                    selectedStudentId = session.selectedStudent.id,
                    onSelect = { id ->
                        sessionViewModel.selectStudent(id)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.RESULTS) {
                ResultsScreen(
                    studentId = session.selectedStudent.id,
                    studentName = session.selectedStudent.name
                )
            }

            composable(Routes.ATTENDANCE) {
                AttendanceScreen(
                    studentId = session.selectedStudent.id,
                    studentName = session.selectedStudent.name,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.TIMETABLE) {
                TimetableScreen(
                    studentId = session.selectedStudent.id,
                    studentName = session.selectedStudent.name
                )
            }

            composable(Routes.NOTICES) {
                NoticesScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.MESSAGES) {
                MessagesScreen(
                    onOpenMessage = { id ->
                        navController.navigate(Routes.messageDetail(id))
                    }
                )
            }

            composable(
                route = Routes.MESSAGE_DETAIL_PATTERN,
                arguments = listOf(
                    navArgument(Routes.ARG_MESSAGE_ID) { type = NavType.StringType }
                )
            ) { entry ->
                val messageId = entry.arguments?.getString(Routes.ARG_MESSAGE_ID).orEmpty()
                MessageDetailScreen(
                    messageId = messageId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    userName = session.user.name,
                    userEmail = session.user.email,
                    onOpenProfile = { navController.navigate(Routes.PROFILE) },
                    onOpenLanguage = { navController.navigate(Routes.LANGUAGE) },
                    onOpenSync = { navController.navigate(Routes.SYNC) },
                    onLogout = { navigateToLogin(navController) },
                    viewModel = settingsViewModel
                )
            }

            composable(Routes.LANGUAGE) {
                LanguageScreen(
                    currentLanguage = settings.language,
                    onConfirm = { code ->
                        settingsViewModel.setLanguage(code)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.SYNC) {
                SyncStatusScreen(onBack = { navController.popBackStack() })
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    user = session.user,
                    linkedStudentsCount = session.students.size,
                    onBack = { navController.popBackStack() },
                    onLogout = { navigateToLogin(navController) }
                )
            }

            composable(Routes.MAPS) {
                MapsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

private fun navigateToLogin(navController: NavHostController) {
    navController.navigate(Routes.LOGIN) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}
