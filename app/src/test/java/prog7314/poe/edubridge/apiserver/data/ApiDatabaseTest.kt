package prog7314.poe.edubridge.apiserver.data

import prog7314.poe.edubridge.apiserver.data.ApiDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Unit tests for the mock API database.

class ApiDatabaseTest {

    @Test
    fun `database has three users`() {
        assertThat(ApiDatabase.users).hasSize(3)
    }

    @Test
    fun `parent has two linked students`() {
        val parent = ApiDatabase.users.first { it.role == "Parent" }
        assertThat(parent.linkedStudentIds).hasSize(2)
    }

    @Test
    fun `results filter by student id`() {
        val liam = ApiDatabase.results.filter { it.studentId == "stu-1" }
        assertThat(liam).hasSize(3)
    }

    @Test
    fun `attendance contains one absent record`() {
        val liam = ApiDatabase.attendance.filter { it.studentId == "stu-1" }
        assertThat(liam.count { it.status == "Absent" }).isEqualTo(1)
    }

    @Test
    fun `notices contain sports day`() {
        assertThat(ApiDatabase.notices.any { it.title.contains("Sports") }).isTrue()
    }

    @Test
    fun `timetable contains mathematics`() {
        assertThat(ApiDatabase.timetable.any { it.subject == "Mathematics" }).isTrue()
    }

    @Test
    fun `settings default exists for parent`() {
        assertThat(ApiDatabase.settings["u-1"]).isNotNull()
        assertThat(ApiDatabase.settings["u-1"]!!.language).isEqualTo("en")
    }

    @Test
    fun `messages populated`() {
        assertThat(ApiDatabase.messages).isNotEmpty()
    }
}