package prog7314.poe.edubridge.ui.screens.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import prog7314.poe.edubridge.data.model.Mark
import prog7314.poe.edubridge.ui.sample.SampleData
import prog7314.poe.edubridge.ui.state.ScreenState

data class ResultsData(
    val term: String,
    val overallGrade: Int,
    val marks: List<Mark>
)

class ResultsViewModel : ViewModel() {

    private val _state = MutableStateFlow<ScreenState<ResultsData>>(ScreenState.Loading)
    val state: StateFlow<ScreenState<ResultsData>> = _state.asStateFlow()

    private var currentStudentId: String? = null
    private var currentTerm: String = "Term 3"

    fun load(studentId: String, term: String = currentTerm) {
        currentStudentId = studentId
        currentTerm = term
        _state.value = ScreenState.Loading
        viewModelScope.launch {
            delay(400)
            val all = SampleData.marksFor(studentId).filter { it.term == term }
            if (all.isEmpty()) {
                _state.value = ScreenState.Empty
                return@launch
            }
            val overall = all.sumOf { it.score } / all.size
            _state.value = ScreenState.Success(
                ResultsData(term = term, overallGrade = overall, marks = all)
            )
        }
    }

    fun selectTerm(term: String) {
        val id = currentStudentId ?: return
        load(id, term)
    }

    fun retry() {
        val id = currentStudentId ?: return
        load(id, currentTerm)
    }
}
