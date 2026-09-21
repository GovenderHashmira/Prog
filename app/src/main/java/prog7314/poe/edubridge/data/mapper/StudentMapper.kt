package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.local.entity.StudentEntity
import prog7314.poe.edubridge.data.model.Student
import prog7314.poe.edubridge.data.remote.dto.StudentDto

fun StudentEntity.toDomain() = Student(
    id = studentId,
    name = "$firstName $lastName".trim(),
    grade = gradeId,
    school = schoolId
)

fun Student.toEntity() = StudentEntity(
    studentId = id,
    userId = null,
    schoolId = school,
    gradeId = grade,
    firstName = name.substringBefore(" ", name),
    lastName = name.substringAfter(" ", ""),
    studentNumber = "",
    active = true
)

fun StudentDto.toDomain() = Student(
    id = studentId,
    name = "$firstName $lastName".trim(),
    grade = gradeName ?: gradeId,
    school = schoolName ?: schoolId
)

fun Student.toDto() = StudentDto(
    studentId = id,
    userId = null,
    schoolId = school,
    gradeId = grade,
    firstName = name.substringBefore(" ", name),
    lastName = name.substringAfter(" ", ""),
    studentNumber = "",
    active = true,
    schoolName = school,
    gradeName = grade
)