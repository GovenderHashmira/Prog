package prog7314.poe.edubridge.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.AttendanceStatus
import prog7314.poe.edubridge.data.model.ClassPeriod
import prog7314.poe.edubridge.data.model.Notice
import prog7314.poe.edubridge.ui.model.WeatherSummary
import prog7314.poe.edubridge.ui.sample.SampleData
import prog7314.poe.edubridge.ui.state.ScreenState
import java.time.LocalDate

data class DashboardData(
    val overallResult: Int,
    val attendanceRate: Int,
    val todayClasses: List<ClassPeriod>,
    val recentNotices: List<Notice>,
    val weather: WeatherSummary
)

class DashboardViewModel : ViewModel() {

    private val _state = MutableStateFlow<ScreenState<DashboardData>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<DashboardData>> = _state.asStateFlow()

    private var loadedStudentId: String? = null

    fun load(studentId: String, weatherCity: String) {
        if (studentId == loadedStudentId && _state.value is ScreenState.Success) return
        loadedStudentId = studentId
        _state.value = ScreenState.Loading
        viewModelScope.launch {
            delay(500)
            val marks = SampleData.marksFor(studentId)
            val attendance = SampleData.attendanceFor(studentId)
            val timetable = SampleData.timetableFor(studentId)
            val today = LocalDate.now().dayOfWeek.value
            val overall = if (marks.isEmpty()) 0 else marks.sumOf { it.score } / marks.size
            val present = attendance.count { it.status == AttendanceStatus.PRESENT }
            val rate = if (attendance.isEmpty()) 0 else present * 100 / attendance.size
            _state.value = ScreenState.Success(
                DashboardData(
                    overallResult = overall,
                    attendanceRate = rate,
                    todayClasses = timetable.periodsForDay(today).ifEmpty { timetable.periods.take(2) },
                    recentNotices = SampleData.notices.take(2),
                    weather = SampleData.weatherFor(weatherCity)
                )
            )
        }
    }

    fun retry(studentId: String, weatherCity: String) {
        loadedStudentId = null
        load(studentId, weatherCity)
    }
}
