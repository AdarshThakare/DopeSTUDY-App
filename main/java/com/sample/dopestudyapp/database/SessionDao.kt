package com.sample.dopestudyapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sample.dopestudyapp.data.Sessions
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session : Sessions)

    @Delete
    suspend fun deleteSession(session: Sessions)

    @Query("SELECT * FROM sessions")
    fun getAllSessions() : Flow<List<Sessions>>

    @Query("SELECT * FROM sessions WHERE SessionSubjectId = :subjectId")
    fun getRecentSessionsForSubject(subjectId : Int) : Flow<List<Sessions>>

    @Query("SELECT SUM(duration) FROM sessions")
    fun getTotalSessionsDuration() : Flow<Long>

    @Query("SELECT SUM(duration) FROM sessions WHERE SessionSubjectId = :subjectId")
    fun getTotalSessionsDurationForSubject(subjectId : Int) : Flow<Long>

    @Query("DELETE FROM sessions WHERE SessionSubjectId = :subjectId")
    suspend fun deleteSessionsBySubjectId(subjectId : Int)
}