package prog7314.poe.edubridge.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import prog7314.poe.edubridge.ui.screens.language.LanguageScreen
import prog7314.poe.edubridge.ui.theme.EduBridgeTheme

class LanguageScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rendersAllSupportedLanguages() {
        composeRule.setContent {
            EduBridgeTheme {
                LanguageScreen(currentLanguage = "en")
            }
        }

        composeRule.onNodeWithText("English").assertIsDisplayed()
        composeRule.onNodeWithText("Afrikaans").assertIsDisplayed()
        composeRule.onNodeWithText("isiZulu").assertIsDisplayed()
    }
}
