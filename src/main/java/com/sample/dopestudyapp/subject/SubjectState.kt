package com.sample.dopestudyapp.subject

import androidx.compose.ui.graphics.Color
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.data.Tasks

data class SubjectState(
    val currentSubjectId: Int? = null,
    val subjectName: String = "",
    val goalStudyHours: String = "",
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val studiedHours: Float = 0f,
    val progress: Float = 0f,
    val recentSessions: List<Sessions> = emptyList(),
    val upcomingTasks: List<Tasks> = emptyList(),
    val completedTasks: List<Tasks> = emptyList(),
    val session: Sessions? = null
)
