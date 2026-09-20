package prog7314.poe.edubridge.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import prog7314.poe.edubridge.ui.screens.messages.MessageDetailScreen
import prog7314.poe.edubridge.ui.theme.EduBridgeTheme

class MessageDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersMessageSubjectAndSender() {
        composeRule.setContent {
            EduBridgeTheme {
                MessageDetailScreen(messageId = "m1")
            }
        }

        composeRule.onNodeWithText("Algebra homework").assertIsDisplayed()
        composeRule.onNodeWithText("Mr. Henderson").assertIsDisplayed()
    }
}
