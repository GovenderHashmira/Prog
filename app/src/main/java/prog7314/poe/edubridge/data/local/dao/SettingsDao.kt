package prog7314.poe.edubridge.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import prog7314.poe.edubridge.data.local.entity.SettingsEntity

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE userId = :userId LIMIT 1")
    fun observe(userId: String): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings LIMIT 1")
    fun observeCurrent(): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE userId = :userId LIMIT 1")
    suspend fun get(userId: String): SettingsEntity?

    @Upsert
    suspend fun upsert(settings: SettingsEntity)

    @Query("DELETE FROM settings")
    suspend fun clear()
}