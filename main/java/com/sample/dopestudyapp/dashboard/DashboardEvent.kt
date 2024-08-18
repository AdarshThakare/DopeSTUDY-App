package com.sample.dopestudyapp.dashboard

import androidx.compose.ui.graphics.Color
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Tasks

sealed class DashboardEvent {

    data object SaveSubject : DashboardEvent()

    data object DeleteSession : DashboardEvent()

    data class OnSubjectNameChange(val name: String) : DashboardEvent()

    data class OnGoalStudyHoursChange(val hours: String) : DashboardEvent()

    data class OnSubjectCardColorChange(val color: List<Color>) : DashboardEvent()

    data class OnTaskIsCompleteChange(val task : Tasks) : DashboardEvent()

    data class OnDeleteSessionButtonClick(val session : Sessions) : DashboardEvent()
}