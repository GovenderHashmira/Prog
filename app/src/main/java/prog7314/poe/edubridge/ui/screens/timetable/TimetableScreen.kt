package prog7314.poe.edubridge.ui.screens.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import prog7314.poe.edubridge.R
import prog7314.poe.edubridge.data.model.ClassPeriod
import prog7314.poe.edubridge.ui.components.EduBridgeTopBar
import prog7314.poe.edubridge.ui.components.EmptyState
import prog7314.poe.edubridge.ui.components.ErrorState
import prog7314.poe.edubridge.ui.components.LoadingIndicator
import prog7314.poe.edubridge.ui.state.ScreenState
import prog7314.poe.edubridge.util.FormatUtils

@Composable
fun TimetableScreen(
    studentId: String,
    studentName: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    viewModel: TimetableViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(studentId) {
        viewModel.load(studentId)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EduBridgeTopBar(
                title = stringResource(R.string.timetable_weekly),
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
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            DaySelectorRow(
                selectedDay = (state as? ScreenState.Success)?.data?.selectedDay ?: 1,
                onDaySelected = viewModel::selectDay
            )
            Spacer(modifier = Modifier.height(12.dp))
            when (val current = state) {
                is ScreenState.Loading -> LoadingIndicator(modifier = Modifier.height(280.dp))
                is ScreenState.Error -> ErrorState(
                    message = current.message,
                    modifier = Modifier.height(280.dp),
                    onRetry = viewModel::retry
                )
                is ScreenState.Empty -> EmptyState(
                    modifier = Modifier.height(280.dp),
                    message = stringResource(R.string.timetable_no_classes)
                )
                is ScreenState.Success -> {
                    if (current.data.periods.isEmpty()) {
                        EmptyState(
                            modifier = Modifier.height(280.dp),
                            message = stringResource(R.string.timetable_no_classes)
                        )
                    } else {
                        Text(
                            text = stringResource(
                                R.string.timetable_classes_count,
                                current.data.periods.size
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            current.data.periods.forEach { period ->
                                PeriodCard(period = period)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DaySelectorRow(
    selectedDay: Int,
    onDaySelected: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (1..5).forEach { day ->
            val selected = day == selectedDay
            val containerColor = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor)
                    .clickable { onDaySelected(day) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = FormatUtils.dayLabel(day),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PeriodCard(period: ClassPeriod) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.width(64.dp)) {
                Text(
                    text = period.startTime.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${period.durationMinutes} min",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = period.subjectName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = period.location ?: "-",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp, end = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = period.teacherName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
