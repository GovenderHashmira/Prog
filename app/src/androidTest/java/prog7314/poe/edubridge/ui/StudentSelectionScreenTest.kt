package prog7314.poe.edubridge.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import prog7314.poe.edubridge.ui.sample.SampleData
import prog7314.poe.edubridge.ui.screens.students.StudentSelectionScreen
import prog7314.poe.edubridge.ui.theme.EduBridgeTheme

class StudentSelectionScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersAllStudents() {
        composeRule.setContent {
            EduBridgeTheme {
                StudentSelectionScreen(
                    students = SampleData.students,
                    selectedStudentId = SampleData.students.first().id
                )
            }
        }

        composeRule.onNodeWithText("Liam Lewis").assertIsDisplayed()
        composeRule.onNodeWithText("Amara Lewis").assertIsDisplayed()
    }

    @Test
    fun selectingStudentInvokesCallback() {
        var selectedId = ""
        composeRule.setContent {
            EduBridgeTheme {
                StudentSelectionScreen(
                    students = SampleData.students,
                    selectedStudentId = SampleData.students.first().id,
                    onSelect = { selectedId = it }
                )
            }
        }

        composeRule.onNodeWithText("Amara Lewis").performClick()
        assertEquals("stu-002", selectedId)
    }
}
