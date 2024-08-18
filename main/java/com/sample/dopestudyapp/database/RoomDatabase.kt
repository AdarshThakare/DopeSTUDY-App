package com.sample.dopestudyapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.data.Tasks

@Database(entities = [Sessions::class, Subject::class, Tasks::class],
          version = 1)
@TypeConverters(ColorListEnabler::class)

abstract class AppDatabase : RoomDatabase(){

    abstract fun sessionsDao(): SessionDao

    abstract fun subjectDao(): SubjectDao

    abstract fun taskDao(): TaskDao
}