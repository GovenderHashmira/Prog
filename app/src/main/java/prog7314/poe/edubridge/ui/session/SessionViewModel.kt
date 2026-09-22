package prog7314.poe.edubridge.ui.session

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import prog7314.poe.edubridge.data.model.Student
import prog7314.poe.edubridge.data.model.User
import prog7314.poe.edubridge.ui.sample.SampleData

data class SessionUiState(
    val user: User = SampleData.currentUser,
    val students: List<Student> = SampleData.students,
    val selectedStudent: Student = SampleData.students.first()
)

class SessionViewModel : ViewModel() {

    private val _state = MutableStateFlow(SessionUiState())
    val state: StateFlow<SessionUiState> = _state.asStateFlow()

    fun selectStudent(studentId: String) {
        val match = _state.value.students.firstOrNull { it.id == studentId } ?: return
        _state.value = _state.value.copy(selectedStudent = match)
    }
}
