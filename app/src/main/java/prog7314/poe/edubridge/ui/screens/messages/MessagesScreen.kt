package prog7314.poe.edubridge.ui.screens.messages

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import prog7314.poe.edubridge.R
import prog7314.poe.edubridge.data.model.Message
import prog7314.poe.edubridge.ui.components.EduBridgeTopBar
import prog7314.poe.edubridge.ui.components.EmptyState
import prog7314.poe.edubridge.ui.components.ErrorState
import prog7314.poe.edubridge.ui.components.LoadingIndicator
import prog7314.poe.edubridge.ui.state.ScreenState
import prog7314.poe.edubridge.util.FormatUtils

@Composable
fun MessagesScreen(
    modifier: Modifier = Modifier,
    onOpenMessage: (String) -> Unit = {},
    viewModel: MessagesViewModel = viewModel()
) {
    val state = viewModel.state

    Scaffold(
        modifier = modifier,
        topBar = { EduBridgeTopBar(title = stringResource(R.string.messages_title)) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val activeFilter = (state as? ScreenState.Success)?.data?.filter ?: MessageFilter.ALL
            FilterRow(
                active = activeFilter,
                onSelect = viewModel::selectFilter
            )
            Box(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is ScreenState.Loading -> LoadingIndicator()
                    is ScreenState.Empty -> EmptyState(
                        message = stringResource(R.string.messages_empty)
                    )
                    is ScreenState.Error -> ErrorState(
                        message = state.message,
                        onRetry = viewModel::retry
                    )
                    is ScreenState.Success -> MessagesList(
                        messages = state.data.messages,
                        onOpenMessage = onOpenMessage
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterRow(active: MessageFilter, onSelect: (MessageFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = active == MessageFilter.ALL,
            onClick = { onSelect(MessageFilter.ALL) },
            label = { Text(stringResource(R.string.messages_filter_all)) }
        )
        FilterChip(
            selected = active == MessageFilter.UNREAD,
            onClick = { onSelect(MessageFilter.UNREAD) },
            label = { Text(stringResource(R.string.messages_filter_unread)) }
        )
    }
}

@Composable
private fun MessagesList(messages: List<Message>, onOpenMessage: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(messages, key = { it.id }) { message ->
            MessageRow(message = message, onClick = { onOpenMessage(message.id) })
        }
    }
}

@Composable
private fun MessageRow(message: Message, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message.senderName.take(1),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.senderName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (message.isUnread) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = FormatUtils.relativeTime(message.sentAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = message.subject,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (message.isUnread) FontWeight.SemiBold else FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = message.preview,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            if (message.isUnread) {
                Spacer(modifier = Modifier.size(8.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = MaterialTheme.colorScheme.tertiary, shape = CircleShape)
                )
            }
        }
    }
}
