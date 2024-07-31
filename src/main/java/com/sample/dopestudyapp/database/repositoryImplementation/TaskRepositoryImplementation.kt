package com.sample.dopestudyapp.database.repositoryImplementation

import com.sample.dopestudyapp.data.Tasks
import com.sample.dopestudyapp.data.repository.TaskRepository
import com.sample.dopestudyapp.database.TaskDao
import com.sample.dopestudyapp.tasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImplementation @Inject constructor(
    private val taskDao: TaskDao
) :TaskRepository {

    override suspend fun upsertTask(task: Tasks) {
        taskDao.upsertTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    override fun getAllTasks(): Flow<List<Tasks>> {
        return taskDao.getAllTasks()
            .map{ tasks -> tasks.filter { it.isCompleted.not()} }
            .map{ tasks -> sortTasks(tasks) }
    }

    override suspend fun getTasksById(taskId: Int): Tasks? {
        return taskDao.getTasksById(taskId)
    }

    override fun getUpcomingTaskForSubject(subjectId: Int): Flow<List<Tasks>> {
        return taskDao.getTaskBySubjectId(subjectId)
            .map { tasks -> tasks.filter { it.isCompleted.not() } }
            .map { tasks -> sortTasks(tasks) }
    }

    override fun getCompletedTaskForSubject(subjectId: Int): Flow<List<Tasks>> {
        return taskDao.getTaskBySubjectId(subjectId)
            .map { tasks -> tasks.filter { it.isCompleted } }
            .map { tasks -> sortTasks(tasks) }
    }

    private fun sortTasks(tasks: List<Tasks>): List<Tasks> {
        return tasks.sortedWith(
            compareBy<Tasks> { it.dueDate }
                .thenBy { it.priority }
        )
    }

}