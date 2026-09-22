package prog7314.poe.edubridge.data

/**
 * Shared enums used across the data layer, repositories and UI.
 * Kept in the root `data` package so all layers can reference them
 * without depending on a specific sub-package.
 */

/** User roles in the system. Enforced server-side; used client-side for UI routing only. */
enum class Role {
    PARENT,
    STUDENT,
    TEACHER,
    ADMIN;

    companion object {
        fun fromString(value: String?): Role =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: STUDENT
    }
}

/** Attendance states for a single student on a single day. */
enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE;

    companion object {
        fun fromString(value: String?): AttendanceStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: ABSENT
    }
}

/** Status of a queued offline operation in the sync queue. */
enum class SyncStatus {
    PENDING,
    SUCCESS,
    FAILED;

    companion object {
        fun fromString(value: String?): SyncStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: PENDING
    }
}

/** Priority of a school notice, used for list ordering and visual emphasis. */
enum class NoticePriority {
    HIGH,
    MEDIUM,
    LOW;

    companion object {
        fun fromString(value: String?): NoticePriority =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MEDIUM
    }
}

/** Type of queued offline operation sent to POST /api/sync. */
enum class OperationType {
    CREATE,
    UPDATE,
    DELETE;

    companion object {
        fun fromString(value: String?): OperationType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: CREATE
    }
}