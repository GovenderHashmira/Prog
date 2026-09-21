package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.MarkEntity
import prog7314.poe.edubridge.data.model.Mark
import prog7314.poe.edubridge.data.remote.dto.ResultDto
import java.time.Instant

fun MarkEntity.toDomain() = Mark(
    id = markId,
    subjectName = subjectName,
    assessmentName = assessmentName,
    score = score,
    period = academicPeriod
)

fun Mark.toEntity(studentId: String, subjectId: String) = MarkEntity(
    markId = id,
    studentId = studentId,
    subjectId = subjectId,
    subjectName = subjectName,
    assessmentName = assessmentName,
    score = score,
    academicPeriod = period,
    updatedAt = Instant.now()
)

fun ResultDto.toDomain() = Mark(
    id = resultId,
    subjectName = subjectName,
    assessmentName = assessmentName,
    score = score,
    period = academicPeriod
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