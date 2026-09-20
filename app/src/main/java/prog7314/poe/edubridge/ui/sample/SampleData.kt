package prog7314.poe.edubridge.ui.sample

import prog7314.poe.edubridge.data.AttendanceStatus
import prog7314.poe.edubridge.data.Role
import prog7314.poe.edubridge.data.model.Attendance
import prog7314.poe.edubridge.data.model.ClassPeriod
import prog7314.poe.edubridge.data.model.Mark
import prog7314.poe.edubridge.data.model.Message
import prog7314.poe.edubridge.data.model.Notice
import prog7314.poe.edubridge.data.model.Student
import prog7314.poe.edubridge.data.model.Timetable
import prog7314.poe.edubridge.data.model.User
import prog7314.poe.edubridge.ui.model.SchoolLocation
import prog7314.poe.edubridge.ui.model.WeatherSummary
import java.time.Instant
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object SampleData {

    val currentUser = User(
        id = "usr-001",
        name = "Sarah Lewis",
        email = "sarah.lewis@example.com",
        role = Role.PARENT
    )

    val students = listOf(
        Student(id = "stu-001", name = "Liam Lewis", grade = "Grade 9", school = "Oakwood International High"),
        Student(id = "stu-002", name = "Amara Lewis", grade = "Grade 6", school = "Maplewood Elementary")
    )

    fun marksFor(studentId: String): List<Mark> = listOf(
        Mark(1, studentId, "Mathematics", 82, "Term 3"),
        Mark(2, studentId, "English Language", 75, "Term 3"),
        Mark(3, studentId, "Natural Science", 79, "Term 3"),
        Mark(4, studentId, "Social Sciences", 81, "Term 3"),
        Mark(5, studentId, "Life Orientation", 88, "Term 3")
    )

    fun attendanceFor(studentId: String): List<Attendance> = listOf(
        Attendance(1, studentId, "2026-08-24", AttendanceStatus.PRESENT, "On time"),
        Attendance(2, studentId, "2026-08-23", AttendanceStatus.PRESENT),
        Attendance(3, studentId, "2026-08-22", AttendanceStatus.LATE, "Arrived 10 minutes late"),
        Attendance(4, studentId, "2026-08-21", AttendanceStatus.PRESENT),
        Attendance(5, studentId, "2026-08-20", AttendanceStatus.ABSENT, "Medical appointment")
    )

    fun timetableFor(studentId: String): Timetable = Timetable(
        id = "tt-$studentId",
        studentId = studentId,
        academicYear = 2026,
        term = 3,
        periods = listOf(
            ClassPeriod("p1", "Chemistry", "Dr. Alan Grant", "Room 301", 3, LocalTime.of(8, 30), LocalTime.of(9, 30)),
            ClassPeriod("p2", "Spanish Language", "Sra. Maria Garcia", "Room 108", 3, LocalTime.of(10, 0), LocalTime.of(11, 0)),
            ClassPeriod("p3", "Study Hall", "Supervised", "Library", 3, LocalTime.of(13, 0), LocalTime.of(14, 0)),
            ClassPeriod("p4", "Mathematics", "Dr. Sarah Jenkins", "Room 214", 1, LocalTime.of(8, 30), LocalTime.of(9, 30)),
            ClassPeriod("p5", "Physics Lab", "Prof. James Chen", "Lab 1", 1, LocalTime.of(9, 45), LocalTime.of(10, 45))
        )
    )

    val notices = listOf(
        Notice(
            id = "n1",
            title = "Term 3 Examination Schedule Released",
            body = "The comprehensive schedule for Term 3 final exams is now available for download. Please check the student portal for the full timetable and venue allocations.",
            audienceRole = null,
            publishedAt = Instant.now().minus(2, ChronoUnit.HOURS)
        ),
        Notice(
            id = "n2",
            title = "Annual Sports Day Registration",
            body = "Sign up for the 100m sprint, relay and high jump are now open. Visit the PE department for entry forms before Friday.",
            audienceRole = null,
            publishedAt = Instant.now().minus(1, ChronoUnit.DAYS)
        ),
        Notice(
            id = "n3",
            title = "School Bus Route Maintenance",
            body = "Route B will have a 15-minute delay tomorrow morning due to scheduled road repairs on High Street.",
            audienceRole = null,
            publishedAt = Instant.now().minus(1, ChronoUnit.DAYS)
        ),
        Notice(
            id = "n4",
            title = "Library Workshop: Research Skills",
            body = "Join us for a session on effective digital research and citation management this Friday at 3:00 PM.",
            audienceRole = null,
            publishedAt = Instant.now().minus(3, ChronoUnit.DAYS),
            isRead = true
        )
    )

    val messages = listOf(
        Message("m1", "Mr. Henderson", "Algebra homework", "Regarding the algebra homework from Tuesday, Liam has shown great progress and is ready for the next module.", Instant.now().minus(3, ChronoUnit.HOURS)),
        Message("m2", "Ms. Rivera", "Library visit", "The class will be visiting the local library this Friday. Please ensure permission slips are signed.", Instant.now().minus(1, ChronoUnit.DAYS)),
        Message("m3", "Office Admin", "Parent-Teacher Conference", "This is a reminder about the upcoming Parent-Teacher Conference scheduled for next week.", Instant.now().minus(2, ChronoUnit.DAYS), readAt = Instant.now().minus(1, ChronoUnit.DAYS)),
        Message("m4", "Coach Miller", "Soccer practice", "Soccer practice has been moved to the indoor gym today due to the weather.", Instant.now().minus(2, ChronoUnit.DAYS), readAt = Instant.now().minus(1, ChronoUnit.DAYS))
    )

    fun messageById(id: String): Message? = messages.firstOrNull { it.id == id }

    val schoolLocation = SchoolLocation(
        name = "Oakwood International High",
        address = "45 Rivonia Road, Sandton, Johannesburg",
        latitude = -26.1076,
        longitude = 28.0567
    )

    fun weatherFor(city: String): WeatherSummary = WeatherSummary(
        city = city,
        temperatureC = 24,
        condition = "Partly Cloudy",
        highC = 27,
        lowC = 15
    )
}
