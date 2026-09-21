package prog7314.poe.edubridge.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import prog7314.poe.edubridge.R
import prog7314.poe.edubridge.data.model.ClassPeriod
import prog7314.poe.edubridge.data.model.Notice
import prog7314.poe.edubridge.data.model.Student
import prog7314.poe.edubridge.ui.components.EduBridgeTopBar
import prog7314.poe.edubridge.ui.components.ErrorState
import prog7314.poe.edubridge.ui.components.LoadingIndicator
import prog7314.poe.edubridge.ui.components.SectionHeader
import prog7314.poe.edubridge.ui.components.SummaryCard
import prog7314.poe.edubridge.ui.components.WeatherCard
import prog7314.poe.edubridge.ui.state.ScreenState
import prog7314.poe.edubridge.util.FormatUtils

@Composable
fun DashboardScreen(
    userName: String,
    selectedStudent: Student,
    modifier: Modifier = Modifier,
    weatherCity: String = "Johannesburg",
    onViewSchedule: () -> Unit = {},
    onSeeAllNotices: () -> Unit = {},
    onNotifications: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    androidx.compose.runtime.LaunchedEffect(selectedStudent.id) {
        viewModel.load(selectedStudent.id, weatherCity)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EduBridgeTopBar(
                title = stringResource(R.string.dashboard_title),
                studentLabel = selectedStudent.name,
                onNotifications = onNotifications
            )
        }
    ) { padding ->
        when (val current = state) {
            is ScreenState.Loading -> LoadingIndicator(modifier = Modifier.padding(padding))
            is ScreenState.Error -> ErrorState(
                message = current.message,
                modifier = Modifier.padding(padding),
                onRetry = { viewModel.retry(selectedStudent.id, weatherCity) }
            )
            is ScreenState.Empty -> LoadingIndicator(modifier = Modifier.padding(padding))
            is ScreenState.Success -> DashboardContent(
                userName = userName,
                studentName = selectedStudent.name,
                data = current.data,
                modifier = Modifier.padding(padding),
                onViewSchedule = onViewSchedule,
                onSeeAllNotices = onSeeAllNotices
            )
        }
    }
}

@Composable
private fun DashboardContent(
    userName: String,
    studentName: String,
    data: DashboardData,
    modifier: Modifier = Modifier,
    onViewSchedule: () -> Unit,
    onSeeAllNotices: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.dashboard_greeting, userName),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.dashboard_subtitle, studentName),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                label = stringResource(R.string.dashboard_academic_overview),
                value = "${data.overallResult}%",
                icon = Icons.Filled.Assessment,
                modifier = Modifier.weight(1f),
                accent = MaterialTheme.colorScheme.primary
            )
            SummaryCard(
                label = stringResource(R.string.dashboard_attendance_rate),
                value = "${data.attendanceRate}%",
                icon = Icons.Filled.CheckCircle,
                modifier = Modifier.weight(1f),
                accent = MaterialTheme.colorScheme.tertiary
            )
        }

        SectionHeader(
            title = stringResource(R.string.dashboard_today_classes),
            actionLabel = stringResource(R.string.dashboard_view_schedule),
            onAction = onViewSchedule,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (data.todayClasses.isEmpty()) {
            Text(
                text = stringResource(R.string.dashboard_no_classes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            data.todayClasses.forEach { period ->
                TodayClassRow(period = period)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        WeatherCard(weather = data.weather)

        SectionHeader(
            title = stringResource(R.string.dashboard_recent_notices),
            actionLabel = stringResource(R.string.dashboard_see_all),
            onAction = onSeeAllNotices,
            modifier = Modifier.padding(top = 8.dp)
        )
        data.recentNotices.forEach { notice ->
            RecentNoticeRow(notice = notice)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun TodayClassRow(period: ClassPeriod) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(20.dp)
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = period.subjectName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = period.location ?: period.teacherName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = period.timeRange,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun RecentNoticeRow(notice: Notice) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text = notice.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = FormatUtils.relativeTime(notice.publishedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
