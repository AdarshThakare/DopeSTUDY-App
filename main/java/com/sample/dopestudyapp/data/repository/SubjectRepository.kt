package com.sample.dopestudyapp.data.repository

import androidx.room.Query
import androidx.room.Upsert
import com.sample.dopestudyapp.data.Subject
import kotlinx.coroutines.flow.Flow

interface SubjectRepository {

    suspend fun upsertSubject(subject: Subject)

    fun getAllSubjects(): Flow<List<Subject>>

    fun getTotalSubjectCount(): Flow<Int>

    fun getTotalGoalHours(): Flow<Float>

    fun getTotalHoursSpent(): Flow<Float>

    suspend fun getSubjectById(subjectId: Int): Subject?

    suspend fun deleteSubjectById(subjectId: Int)
}