package prog7314.poe.edubridge.ui.screens.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.AttendanceStatus
import prog7314.poe.edubridge.data.model.Attendance
import prog7314.poe.edubridge.ui.sample.SampleData
import prog7314.poe.edubridge.ui.state.ScreenState

data class AttendanceData(
    val overallRate: Int,
    val presentCount: Int,
    val absentCount: Int,
    val lateCount: Int,
    val records: List<Attendance>
)

class AttendanceViewModel : ViewModel() {

    private val _state = MutableStateFlow<ScreenState<AttendanceData>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<AttendanceData>> = _state.asStateFlow()

    private var currentStudentId: String? = null

    fun load(studentId: String) {
        currentStudentId = studentId
        _state.value = ScreenState.Loading
        viewModelScope.launch {
            delay(400)
            val records = SampleData.attendanceFor(studentId)
            if (records.isEmpty()) {
                _state.value = ScreenState.Empty
                return@launch
            }
            val present = records.count { it.status == AttendanceStatus.PRESENT }
            val absent = records.count { it.status == AttendanceStatus.ABSENT }
            val late = records.count { it.status == AttendanceStatus.LATE }
            val rate = present * 100 / records.size
            _state.value = ScreenState.Success(
                AttendanceData(
                    overallRate = rate,
                    presentCount = present,
                    absentCount = absent,
                    lateCount = late,
                    records = records
                )
            )
        }
    }

    fun retry() {
        val id = currentStudentId ?: return
        load(id)
    }
}
