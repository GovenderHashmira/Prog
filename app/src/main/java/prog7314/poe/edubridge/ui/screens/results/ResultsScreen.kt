package prog7314.poe.edubridge.ui.screens.results

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import prog7314.poe.edubridge.R
import prog7314.poe.edubridge.data.model.Mark
import prog7314.poe.edubridge.ui.components.EduBridgeTopBar
import prog7314.poe.edubridge.ui.components.EmptyState
import prog7314.poe.edubridge.ui.components.ErrorState
import prog7314.poe.edubridge.ui.components.LoadingIndicator
import prog7314.poe.edubridge.ui.components.SectionHeader
import prog7314.poe.edubridge.ui.state.ScreenState

@Composable
fun ResultsScreen(
    studentId: String,
    studentName: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    viewModel: ResultsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(studentId) {
        viewModel.load(studentId)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EduBridgeTopBar(
                title = stringResource(R.string.results_title),
                studentLabel = studentName,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            TermFilterRow(
                selectedTerm = (state as? ScreenState.Success)?.data?.term ?: "Term 3",
                onTermSelected = viewModel::selectTerm
            )
            Spacer(modifier = Modifier.height(12.dp))
            when (val current = state) {
                is ScreenState.Loading -> LoadingIndicator(modifier = Modifier.height(240.dp))
                is ScreenState.Empty -> EmptyState(modifier = Modifier.height(240.dp))
                is ScreenState.Error -> ErrorState(
                    message = current.message,
                    modifier = Modifier.height(240.dp),
                    onRetry = viewModel::retry
                )
                is ScreenState.Success -> {
                    CurrentStandingCard(overall = current.data.overallGrade)
                    SectionHeader(
                        title = stringResource(R.string.results_subject_breakdown),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    current.data.marks.forEach { mark ->
                        SubjectMarkCard(mark = mark)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun TermFilterRow(
    selectedTerm: String,
    onTermSelected: (String) -> Unit
) {
    val terms = listOf(
        "Term 1" to stringResource(R.string.results_filter_term1),
        "Term 2" to stringResource(R.string.results_filter_term2),
        "Term 3" to stringResource(R.string.results_filter_term3),
        "Final" to stringResource(R.string.results_filter_final)
    )
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        terms.forEach { (key, label) ->
            FilterChip(
                selected = key == selectedTerm,
                onClick = { onTermSelected(key) },
                label = { Text(text = label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun CurrentStandingCard(overall: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.results_current_standing).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
            Text(
                text = "$overall%",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = stringResource(R.string.results_overall_grade),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun SubjectMarkCard(mark: Mark) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mark.subject,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${mark.score}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = mark.score / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
