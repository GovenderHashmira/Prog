package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.MessageEntity
import prog7314.poe.edubridge.data.model.Message
import prog7314.poe.edubridge.data.remote.dto.MessageDto
import java.time.Instant

fun MessageEntity.toDomain() = Message(
    id = messageId,
    senderName = senderName,
    subject = subject,
    body = body,
    sentAt = sentAt,
    readAt = readAt
)

fun MessageDto.toEntity() = MessageEntity(
    messageId = messageId,
    senderId = senderId,
    senderName = senderName,
    recipientId = recipientId,
    subject = subject,
    body = body,
    sentAt = runCatching { Instant.parse(sentAt) }.getOrDefault(Instant.now()),
    readAt = readAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
)

fun MessageDto.toDomain() = Message(
    id = messageId,
    senderName = senderName,
    subject = subject,
    body = body,
    sentAt = runCatching { Instant.parse(sentAt) }.getOrDefault(Instant.now()),
    readAt = readAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
)