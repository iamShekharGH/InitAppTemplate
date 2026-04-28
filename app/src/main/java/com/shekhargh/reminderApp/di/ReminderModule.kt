package com.shekhargh.reminderApp.di

import android.content.Context
import androidx.room.Room
import com.shekhargh.reminderApp.data.db.ReminderDatabase
import com.shekhargh.reminderApp.data.db.RemindersDao
import com.shekhargh.reminderApp.data.repo.RemindersRepositoryImpl
import com.shekhargh.reminderApp.databaseTable
import com.shekhargh.reminderApp.domain.RemindersRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReminderModule {

    @Binds
    @Singleton
    abstract fun bindsRepoToImpl(remindersRepositoryImpl: RemindersRepositoryImpl): RemindersRepository


    companion object {
        @Provides
        @Singleton
        fun providesRoomDatabase(@ApplicationContext applicationContext: Context): ReminderDatabase {
            return Room.databaseBuilder(
                context = applicationContext,
                klass = ReminderDatabase::class.java,
                name = databaseTable
            ).allowMainThreadQueries().build()

        }

        @Provides
        @Singleton
        fun providesDao(database: ReminderDatabase): RemindersDao {
            return database.getReminderDao()
        }
    }


}