package prog7314.poe.edubridge.apiserver.routes

import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Unit tests for student visibility rules.

class StudentRoutesTest {

    @Test
    fun `parent sees only linked students`() {
        val parent = ApiDatabase.users.first { it.role == "Parent" }
        val visible = ApiDatabase.students.filter { it.id in parent.linkedStudentIds }
        assertThat(visible).hasSize(2)
    }

    @Test
    fun `student sees only own record`() {
        val student = ApiDatabase.users.first { it.role == "Student" }
        val visible = ApiDatabase.students.filter { it.id in student.linkedStudentIds }
        assertThat(visible).hasSize(1)
        assertThat(visible.first().name).isEqualTo("Liam Lewis")
    }

    @Test
    fun `teacher sees all students`() {
        val teacher = ApiDatabase.users.first { it.role == "Teacher" }
        val visible = when (teacher.role) {
            "Teacher", "Admin" -> ApiDatabase.students
            else -> emptyList()
        }
        assertThat(visible).hasSize(2)
    }

    @Test
    fun `student lookup by id works`() {
        val s = ApiDatabase.students.firstOrNull { it.id == "stu-1" }
        assertThat(s).isNotNull()
        assertThat(s!!.studentNumber).isEqualTo("STU001")
    }
}