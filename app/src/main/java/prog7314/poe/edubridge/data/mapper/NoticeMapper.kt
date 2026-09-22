package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.NoticeEntity
import prog7314.poe.edubridge.data.model.Notice
import prog7314.poe.edubridge.data.remote.dto.NoticeDto
import java.time.Instant

fun NoticeEntity.toDomain() = Notice(
    id = noticeId,
    title = title,
    body = body,
    audienceRole = audienceRole,
    gradeId = gradeId,
    publishedAt = publishedAt,
    expiresAt = expiresAt,
    isRead = isRead
)

fun Notice.toEntity() = NoticeEntity(
    noticeId = id,
    title = title,
    body = body,
    audienceRole = audienceRole,
    gradeId = gradeId,
    priority = prog7314.poe.edubridge.data.NoticePriority.MEDIUM,
    publishedAt = publishedAt,
    expiresAt = expiresAt,
    isRead = isRead
)

fun NoticeDto.toDomain() = Notice(
    id = noticeId,
    title = title,
    body = body,
    audienceRole = audienceRole,
    gradeId = gradeId,
    publishedAt = runCatching { Instant.parse(publishedAt) }.getOrDefault(Instant.now()),
    expiresAt = expiresAt?.let { runCatching { Instant.parse(it) }.getOrNull() },
    isRead = isRead
)

fun NoticeDto.toEntity() = NoticeEntity(
    noticeId = noticeId,
    title = title,
    body = body,
    audienceRole = audienceRole,
    gradeId = gradeId,
    priority = prog7314.poe.edubridge.data.NoticePriority.fromString(priority),
    publishedAt = runCatching { Instant.parse(publishedAt) }.getOrDefault(Instant.now()),
    expiresAt = expiresAt?.let { runCatching { Instant.parse(it) }.getOrNull() },
    isRead = isRead
)