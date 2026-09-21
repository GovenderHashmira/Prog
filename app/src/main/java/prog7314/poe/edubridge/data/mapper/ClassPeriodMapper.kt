package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.ClassPeriodEntity
import prog7314.poe.edubridge.data.model.ClassPeriod
import prog7314.poe.edubridge.data.remote.dto.ClassPeriodDto
import java.time.LocalTime

fun ClassPeriodEntity.toDomain() = ClassPeriod(
    id = periodId,
    subjectName = subjectName,
    teacherName = teacherName,
    location = location,
    dayOfWeek = dayOfWeek,
    startTime = runCatching { LocalTime.parse(startTime) }.getOrDefault(LocalTime.MIN),
    endTime = runCatching { LocalTime.parse(endTime) }.getOrDefault(LocalTime.MAX)
)

fun ClassPeriodDto.toEntity() = ClassPeriodEntity(
    periodId = periodId,
    timetableId = timetableId,
    subjectId = subjectId,
    subjectName = subjectName,
    teacherName = teacherName,
    location = location,
    dayOfWeek = dayOfWeek,
    startTime = startTime,
    endTime = endTime
)

fun ClassPeriodDto.toDomain() = ClassPeriod(
    id = periodId,
    subjectName = subjectName,
    teacherName = teacherName,
    location = location,
    dayOfWeek = dayOfWeek,
    startTime = runCatching { LocalTime.parse(startTime) }.getOrDefault(LocalTime.MIN),
    endTime = runCatching { LocalTime.parse(endTime) }.getOrDefault(LocalTime.MAX)
)