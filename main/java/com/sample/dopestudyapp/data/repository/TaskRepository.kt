package com.sample.dopestudyapp.data.repository

import androidx.room.Query
import androidx.room.Upsert
import com.sample.dopestudyapp.data.Tasks
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun upsertTask(task: Tasks)

    suspend fun deleteTask(taskId: Int)

    fun getAllTasks(): Flow<List<Tasks>>

    suspend fun getTasksById(taskId: Int): Tasks?

    fun getUpcomingTaskForSubject(subjectId: Int): Flow<List<Tasks>>

    fun getCompletedTaskForSubject(subjectId: Int): Flow<List<Tasks>>
}