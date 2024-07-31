package com.sample.dopestudyapp.subject

import androidx.compose.ui.graphics.Color
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Tasks

sealed class SubjectEvent {
    data object UpdateSubject : SubjectEvent()
    data object DeleteSubject : SubjectEvent()
    data object DeleteSession : SubjectEvent()
    data object UpdateProgress : SubjectEvent()
    data class OnTaskIsCompleteChange(val task: Tasks): SubjectEvent()
    data class OnSubjectCardColorChange(val color: List<Color>): SubjectEvent()
    data class OnSubjectNameChange(val name: String): SubjectEvent()
    data class OnGoalStudyHoursChange(val hours: String): SubjectEvent()
    data class OnDeleteSessionButtonClick(val session: Sessions): SubjectEvent()
}