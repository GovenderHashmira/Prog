package prog7314.poe.edubridge.apiserver

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

// simple in-memory token store
// production systems use signed JWTs.
// demonstrates role-based authentication

object AuthTokenStore {
    // TOKEN -> useId
    private val tokens = ConcurrentHashMap<String, String>()

    // token -> role
    private val roles = ConcurrentHashMap<String, String>()

    fun issueToken(userId: String, role: String): String {
        val token = "ebr-${UUID.randomUUID()}"
        tokens[token] = userId
        roles[token] = role
        ApiLogger.i("Issued token for userId=$userId role=$role")
        return token
    }

    fun userIdFor(token: String?): String? {
        if (token.isNullOrBlank()) return null
        return tokens[token.removePrefix("Bearer ").trim()]
    }

    fun roleFor(token: String?): String? {
        if (token.isNullOrBlank()) return null
        return roles[token.removePrefix("Bearer ").trim()]
    }

    fun revoke(token: String) {
        val cleaned = token.removePrefix("Bearer ").trim()
        tokens.remove(cleaned)
        roles.remove(cleaned)
        ApiLogger.i("Revoked token")
    }

    fun clear() {
        tokens.clear()
        roles.clear()
    }
}