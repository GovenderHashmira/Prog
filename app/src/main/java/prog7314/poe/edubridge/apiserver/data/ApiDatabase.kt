package prog7314.poe.edubridge.apiserver.data

import prog7314.poe.edubridge.apiserver.models.*
import prog7314.poe.edubridge.apiserver.data.*


// In-memory mock data database for the EduBridge REST API
//Serves as the authoritative data source fpr the app.

object ApiDatabase {

    val users: MutableList<ApiUser> = mutableListOf(
        ApiUser(
            id = "u-1",
            name = "Sarah Lewis",
            email = "sarah@edubridge.com",
            password = "password123",
            role = "Parent",
            linkedStudentIds = listOf("stu-1", "stu-2")
        ),
        ApiUser(
            id = "u-2",
            name = "Liam Lewis",
            email = "liam@edubridge.com",
            password = "password123",
            role = "Student",
            linkedStudentIds = listOf("stu-1")
        ),
        ApiUser(
            id = "u-3",
            name = "Mr Henderson",
            email = "teacher@edubridge.com",
            password = "password123",
            role = "Teacher",
            linkedStudentIds = emptyList()
        )
    )

    val students: MutableList<ApiStudent> = mutableListOf(
        ApiStudent("stu-1", "Liam Lewis",  "10A", "sch-1", "STU001"),
        ApiStudent("stu-2", "Amara Lewis", "8B",  "sch-1", "STU002")
    )

    val results: MutableList<ApiResult> = mutableListOf(
        ApiResult("res-1", "stu-1", "Mathematics", 82.0, "Term 1 Exam", "2026-Term1", "Excellent progress", "2026-09-15"),
        ApiResult("res-2", "stu-1", "English",     75.0, "Term 1 Exam", "2026-Term1", "Good effort",        "2026-09-15"),
        ApiResult("res-3", "stu-1", "Science",     79.0, "Term 1 Exam", "2026-Term1", "",                   "2026-09-15"),
        ApiResult("res-4", "stu-2", "Mathematics", 78.0, "Term 1 Exam", "2026-Term1", "",                   "2026-09-15"),
        ApiResult("res-5", "stu-2", "English",     88.0, "Term 1 Exam", "2026-Term1", "Outstanding",        "2026-09-15")
    )

    val attendance: MutableList<ApiAttendance> = mutableListOf(
        ApiAttendance("att-1", "stu-1", "2026-09-01", "Present"),
        ApiAttendance("att-2", "stu-1", "2026-09-02", "Present"),
        ApiAttendance("att-3", "stu-1", "2026-09-03", "Absent", "Sick note provided"),
        ApiAttendance("att-4", "stu-1", "2026-09-04", "Present"),
        ApiAttendance("att-5", "stu-2", "2026-09-01", "Present"),
        ApiAttendance("att-6", "stu-2", "2026-09-02", "Late",   "Arrived 15 min late")
    )

    val timetable: MutableList<ApiTimetable> = mutableListOf(
        ApiTimetable("tt-1", "stu-1", "Monday",  "08:00", "09:00", "Mathematics", "Mr Henderson", "R-101"),
        ApiTimetable("tt-2", "stu-1", "Monday",  "09:00", "10:00", "English",     "Ms Botha",     "R-204"),
        ApiTimetable("tt-3", "stu-1", "Tuesday", "08:00", "09:00", "Science",     "Dr Nkosi",     "Lab-1"),
        ApiTimetable("tt-4", "stu-2", "Monday",  "08:00", "09:00", "isiZulu",     "Mrs Dlamini",  "R-105")
    )

    val notices: MutableList<ApiNotice> = mutableListOf(
        ApiNotice("n-1", "Sports Day 2026",        "Annual sports day on Friday 25 September.",   "2026-09-15"),
        ApiNotice("n-2", "Term 3 Exams",           "Exam timetable now available on the portal.", "2026-09-10"),
        ApiNotice("n-3", "Parent-Teacher Meeting", "Scheduled for next Wednesday at 15:00.",      "2026-09-08")
    )

    val messages: MutableList<ApiMessage> = mutableListOf(
        ApiMessage("m-1", "Mr. Henderson", "Teacher", "Great progress", "Liam is doing really well.",  "2026-09-14", true),
        ApiMessage("m-2", "Ms. Botha",     "Teacher", "Library visit",  "Class will visit the library.","2026-09-13", false)
    )

    val settings: MutableMap<String, ApiUserSettings> = mutableMapOf(
        "u-1" to ApiUserSettings("u-1", "en", true, false)
    )
}