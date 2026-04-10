package com.shekhargh.reminderApp.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Upsert
import com.shekhargh.reminderApp.reminderTable
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Entity(tableName = reminderTable)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String = "",
    val completionStatus: Boolean = false,
    val priority: Priority,
    val dueDate: LocalDateTime = LocalDateTime.now()
)

@Database(entities = [Reminder::class], version = 1, exportSchema = false)
@TypeConverters(ReminderAppTypeConverters::class)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun getReminderDao(): RemindersDao
}

@Dao
interface RemindersDao {

    @Upsert
    suspend fun upsertReminder(reminder: Reminder): Long

    @Query("DELETE FROM reminder_table WHERE id = :id")
    suspend fun deleteReminder(id: Int): Int

    @Query("SELECT * FROM reminder_table WHERE id=:id")
    suspend fun getItemFromId(id: Int): Reminder?

    @Query("UPDATE reminder_table SET completionStatus = :isComplete WHERE id=:id")
    suspend fun updateCompletionStatus(isComplete: Boolean, id: Int)

    @Query("SELECT * FROM reminder_table ORDER BY dueDate ASC")
    fun getAllReminders(): Flow<List<Reminder>>

}

enum class Priority {
    LOW, MEDIUM, HIGH
}
