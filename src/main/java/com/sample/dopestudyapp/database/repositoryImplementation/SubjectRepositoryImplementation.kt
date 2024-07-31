package com.sample.dopestudyapp.database.repositoryImplementation

import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.data.repository.SubjectRepository
import com.sample.dopestudyapp.database.SessionDao
import com.sample.dopestudyapp.database.SubjectDao
import com.sample.dopestudyapp.database.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubjectRepositoryImplementation @Inject constructor(

    //linking implementation to repository using dao
    private val subjectDao: SubjectDao,
    private val taskDao: TaskDao,
    private val sessionDao: SessionDao
) : SubjectRepository{

    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject)
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }


    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalHoursSpent(): Flow<Float> {
        return subjectDao.getTotalHoursSpent()
    }

    override suspend fun getSubjectById(subjectId: Int): Subject? {
        return subjectDao.getSubjectById(subjectId)
    }

    override suspend fun deleteSubjectById(subjectId: Int) {
        taskDao.deleteTaskBySubjectId(subjectId)
        sessionDao.deleteSessionsBySubjectId(subjectId)
        subjectDao.deleteSubjectById(subjectId)
    }

}