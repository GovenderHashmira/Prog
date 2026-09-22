package prog7314.poe.edubridge.data.mapper

import prog7314.poe.edubridge.data.Role
import prog7314.poe.edubridge.data.local.entity.UserEntity
import prog7314.poe.edubridge.data.model.User
import prog7314.poe.edubridge.data.remote.dto.UserProfileDto
import java.time.Instant

fun UserEntity.toDomain() = User(
    id = userId,
    name = name,
    email = email,
    role = role,
    language = language
)

fun User.toEntity(createdAt: Instant = Instant.now()) = UserEntity(
    userId = id,
    name = name,
    email = email,
    role = role,
    language = language,
    createdAt = createdAt,
    updatedAt = Instant.now()
)

fun UserProfileDto.toDomain() = User(
    id = userId,
    name = name,
    email = email,
    role = Role.fromString(role),
    language = language
)

fun UserProfileDto.toEntity() = UserEntity(
    userId = userId,
    name = name,
    email = email,
    role = Role.fromString(role),
    language = language,
    createdAt = Instant.now(),
    updatedAt = Instant.now()
)