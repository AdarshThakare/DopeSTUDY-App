package com.sample.dopestudyapp.data.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sample.dopestudyapp.data.Sessions
import kotlinx.coroutines.flow.Flow

interface SessionsRepository {

    suspend fun insertSession(session : Sessions)

    suspend fun deleteSession(session: Sessions)

    fun getAllSessions() : Flow<List<Sessions>>

    fun getRecentFiveSessions() : Flow<List<Sessions>>

    fun getRecentSessionsForSubject(subjectId : Int) : Flow<List<Sessions>>

    fun getTotalSessionsDuration() : Flow<Long>

    fun getTotalSessionsDurationForSubject(subjectId : Int) : Flow<Long>

}