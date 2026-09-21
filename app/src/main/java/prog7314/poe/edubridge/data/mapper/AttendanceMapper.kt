package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.AttendanceStatus
import prog7314.poe.edubridge.data.local.entity.AttendanceEntity
import prog7314.poe.edubridge.data.model.Attendance
import prog7314.poe.edubridge.data.remote.dto.AttendanceDto
import java.time.Instant

fun AttendanceEntity.toDomain() = Attendance(
    id = attendanceId,
    date = date,
    status = status,
    comment = notes
)

fun Attendance.toEntity(studentId: String) = AttendanceEntity(
    attendanceId = id,
    studentId = studentId,
    date = date,
    status = status,
    notes = comment,
    updatedAt = Instant.now()
)

fun AttendanceDto.toDomain() = Attendance(
    id = attendanceId,
    date = date,
    status = AttendanceStatus.fromString(status),
    comment = notes
)

fun AttendanceDto.toEntity() = AttendanceEntity(
    attendanceId = attendanceId,
    studentId = studentId,
    date = date,
    status = AttendanceStatus.fromString(status),
    notes = notes,
    updatedAt = runCatching { Instant.parse(updatedAt) }.getOrDefault(Instant.now())
)