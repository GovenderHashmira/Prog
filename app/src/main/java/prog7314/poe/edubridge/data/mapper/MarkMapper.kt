package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.MarkEntity
import prog7314.poe.edubridge.data.model.Mark
import prog7314.poe.edubridge.data.remote.dto.ResultDto
import java.time.Instant

fun MarkEntity.toDomain() = Mark(
    id = markId.hashCode(),
    studentId = studentId,
    subject = subjectName,
    score = score.toInt(),
    term = academicPeriod
)

fun Mark.toEntity(subjectId: String) = MarkEntity(
    markId = id.toString(),
    studentId = studentId,
    subjectId = subjectId,
    subjectName = subject,
    assessmentName = term,
    score = score.toDouble(),
    academicPeriod = term,
    updatedAt = Instant.now()
)

fun ResultDto.toDomain() = Mark(
    id = resultId.hashCode(),
    studentId = studentId,
    subject = subjectName,
    score = score.toInt(),
    term = academicPeriod
)

fun ResultDto.toEntity() = MarkEntity(
    markId = resultId,
    studentId = studentId,
    subjectId = subjectId,
    subjectName = subjectName,
    assessmentName = assessmentName,
    score = score,
    academicPeriod = academicPeriod,
    updatedAt = runCatching { Instant.parse(updatedAt) }.getOrDefault(Instant.now())
)