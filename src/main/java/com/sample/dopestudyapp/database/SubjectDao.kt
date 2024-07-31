package com.sample.dopestudyapp.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sample.dopestudyapp.data.Subject
import kotlinx.coroutines.flow.Flow

//Database CRUD Operations - Step 2

@Dao
interface SubjectDao {

    //suspend function is used for function that requires data change only when an action is performed
    //flow<> is used when we need to monitor a field throughout the apps functioning even without an action like a button click

    @Upsert //Updates the value if exists/ Inserts the value if not not exists
    suspend fun upsertSubject(subject: Subject)

    @Query("SELECT * FROM subject")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT COUNT(*) FROM subject")
    fun getTotalSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM subject")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT SUM(goalHours) FROM subject")
    fun getTotalHoursSpent(): Flow<Float>

    @Query("SELECT * FROM subject WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): Subject?

    @Query("DELETE FROM subject WHERE subjectId = :subjectId")
    suspend fun deleteSubjectById(subjectId: Int)
}