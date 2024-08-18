package com.sample.dopestudyapp.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sample.dopestudyapp.data.Tasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: Tasks)

    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("DELETE FROM tasks WHERE taskSubjectId = :subjectId")
    suspend fun deleteTaskBySubjectId(subjectId: Int)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Tasks>>

    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    suspend fun getTasksById(taskId: Int): Tasks?

    @Query("SELECT * FROM tasks WHERE taskSubjectId = :subjectId")
    fun getTaskBySubjectId(subjectId: Int): Flow<List<Tasks>>
}