package com.shekhargh.reminderApp.data.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ReminderAppTypeConverters {

    @TypeConverter
    fun priorityToString(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun stringToPriority(priorityString: String): Priority {
        return Priority.valueOf(priorityString)
    }

    @TypeConverter
    fun localDateTimeToString(localDateTime: LocalDateTime): String {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli().toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(localDateTime: String): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(localDateTime.toLong()), ZoneOffset.UTC)
    }


}