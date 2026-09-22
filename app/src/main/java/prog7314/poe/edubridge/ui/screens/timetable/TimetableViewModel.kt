package prog7314.poe.edubridge.ui.screens.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.model.ClassPeriod
import prog7314.poe.edubridge.ui.sample.SampleData
import prog7314.poe.edubridge.ui.state.ScreenState
import java.time.LocalDate

data class TimetableData(
    val selectedDay: Int,
    val periods: List<ClassPeriod>
)

class TimetableViewModel : ViewModel() {

    private val _state = MutableStateFlow<ScreenState<TimetableData>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<TimetableData>> = _state.asStateFlow()

    private var currentStudentId: String? = null
    private var selectedDay: Int = defaultDay()

    private fun defaultDay(): Int {
        val today = LocalDate.now().dayOfWeek.value
        return if (today in 1..5) today else 1
    }

    fun load(studentId: String, day: Int = selectedDay) {
        currentStudentId = studentId
        selectedDay = day
        _state.value = ScreenState.Loading
        viewModelScope.launch {
            delay(300)
            val timetable = SampleData.timetableFor(studentId)
            _state.value = ScreenState.Success(
                TimetableData(selectedDay = day, periods = timetable.periodsForDay(day))
            )
        }
    }

    fun selectDay(day: Int) {
        val id = currentStudentId ?: return
        load(id, day)
    }

    fun retry() {
        val id = currentStudentId ?: return
        load(id, selectedDay)
    }
}
