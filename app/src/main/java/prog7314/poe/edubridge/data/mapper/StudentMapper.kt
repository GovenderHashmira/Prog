package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.StudentEntity
import prog7314.poe.edubridge.data.model.Student
import prog7314.poe.edubridge.data.remote.dto.StudentDto

fun StudentEntity.toDomain() = Student(
    id = studentId,
    fullName = "$firstName $lastName".trim(),
    gradeName = gradeId,
    schoolName = schoolId,
    studentNumber = studentNumber,
    active = active
)

fun Student.toEntity() = StudentEntity(
    studentId = id,
    userId = null,
    schoolId = schoolName,
    gradeId = gradeName,
    firstName = fullName.substringBefore(" ", fullName),
    lastName = fullName.substringAfter(" ", ""),
    studentNumber = studentNumber,
    active = active
)

fun StudentDto.toDomain() = Student(
    id = studentId,
    fullName = "$firstName $lastName".trim(),
    gradeName = gradeName ?: gradeId,
    schoolName = schoolName ?: schoolId,
    studentNumber = studentNumber,
    active = active
)

fun Student.toDto() = StudentDto(
    studentId = id,
    userId = null,
    schoolId = schoolName,
    gradeId = gradeName,
    firstName = fullName.substringBefore(" ", fullName),
    lastName = fullName.substringAfter(" ", ""),
    studentNumber = studentNumber,
    active = active,
    schoolName = schoolName,
    gradeName = gradeName
)