package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.TimetableEntity
import prog7314.poe.edubridge.data.model.Timetable
import prog7314.poe.edubridge.data.remote.dto.TimetableDto

fun TimetableEntity.toDomain(periods: List<prog7314.poe.edubridge.data.model.ClassPeriod>) = Timetable(
    id = timetableId,
    studentId = studentId,
    academicYear = academicYear,
    term = term,
    periods = periods
)

fun TimetableDto.toEntity() = TimetableEntity(
    timetableId = timetableId,
    studentId = studentId,
    academicYear = academicYear,
    term = term
)

fun TimetableDto.toDomain(periods: List<prog7314.poe.edubridge.data.model.ClassPeriod>) = Timetable(
    id = timetableId,
    studentId = studentId,
    academicYear = academicYear,
    term = term,
    periods = periods
)