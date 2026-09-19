package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Unit tests for academic data filtering.

class AcademicRoutesTest {

    @Test
    fun `results filtered by liam id return three`() {
        val r = ApiDatabase.results.filter { it.studentId == "stu-1" }
        assertThat(r).hasSize(3)
    }

    @Test
    fun `results filtered by amara id return two`() {
        val r = ApiDatabase.results.filter { it.studentId == "stu-2" }
        assertThat(r).hasSize(2)
    }

    @Test
    fun `results contain expected subjects`() {
        val subjects = ApiDatabase.results
            .filter { it.studentId == "stu-1" }
            .map { it.subject }
        assertThat(subjects).containsAtLeast("Mathematics", "English", "Science")
    }

    @Test
    fun `marks are within 0 to 100`() {
        ApiDatabase.results.forEach {
            assertThat(it.mark).isAtLeast(0.0)
            assertThat(it.mark).isAtMost(100.0)
        }
    }

    @Test
    fun `attendance filtered by student`() {
        val a = ApiDatabase.attendance.filter { it.studentId == "stu-1" }
        assertThat(a).hasSize(4)
    }

    @Test
    fun `attendance statuses are valid`() {
        val valid = setOf("Present", "Absent", "Late")
        ApiDatabase.attendance.forEach {
            assertThat(valid).contains(it.status)
        }
    }

    @Test
    fun `timetable filtered by student`() {
        val t = ApiDatabase.timetable.filter { it.studentId == "stu-1" }
        assertThat(t).hasSize(3)
    }

    @Test
    fun `timetable entries have start and end times`() {
        ApiDatabase.timetable.forEach {
            assertThat(it.startTime).isNotEmpty()
            assertThat(it.endTime).isNotEmpty()
        }
    }

    @Test
    fun `unknown student has empty results`() {
        val r = ApiDatabase.results.filter { it.studentId == "stu-999" }
        assertThat(r).isEmpty()
    }
}