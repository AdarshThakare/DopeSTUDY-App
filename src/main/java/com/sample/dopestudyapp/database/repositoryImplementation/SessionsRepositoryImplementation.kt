package com.sample.dopestudyapp.database.repositoryImplementation

import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.repository.SessionsRepository
import com.sample.dopestudyapp.database.SessionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class SessionsRepositoryImplementation @Inject constructor(
    private val sessionDao: SessionDao
): SessionsRepository {

    override suspend fun insertSession(session: Sessions) {
        sessionDao.insertSession(session)
    }

    override suspend fun deleteSession(session: Sessions) {
        sessionDao.deleteSession(session)
    }

    override fun getAllSessions(): Flow<List<Sessions>> {
        return sessionDao.getAllSessions()
    }

    override fun getRecentFiveSessions(): Flow<List<Sessions>> {
        return sessionDao.getAllSessions().take(5)
    }

    override fun getRecentSessionsForSubject(subjectId: Int): Flow<List<Sessions>> {
        return sessionDao.getRecentSessionsForSubject(subjectId)
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    override fun getTotalSessionsDurationForSubject(subjectId: Int): Flow<Long> {
        return sessionDao.getTotalSessionsDurationForSubject(subjectId)
    }


}