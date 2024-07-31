package com.sample.dopestudyapp.daghilt

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sample.dopestudyapp.database.AppDatabase
import com.sample.dopestudyapp.database.SessionDao
import com.sample.dopestudyapp.database.SubjectDao
import com.sample.dopestudyapp.database.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): AppDatabase {

        // creation of app database object
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "studysmart.db"
        ).build()

    }

    @Provides
    @Singleton
    fun provideSubjectDao(
        database: AppDatabase
    ): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDao(
        database: AppDatabase
    ): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideSessionsDao(
        database: AppDatabase
    ): SessionDao {
        return database.sessionsDao()
    }
}