package com.sample.dopestudyapp.dashboard

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.data.Tasks
import com.sample.dopestudyapp.data.repository.SessionsRepository
import com.sample.dopestudyapp.data.repository.SubjectRepository
import com.sample.dopestudyapp.data.repository.TaskRepository
import com.sample.dopestudyapp.enums.SnackBarEvent
import com.sample.dopestudyapp.enums.toHours
import com.sample.dopestudyapp.tasks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val sessionsRepository: SessionsRepository,
    private val taskRepository: TaskRepository
): ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        subjectRepository.getTotalSubjectCount(),
        subjectRepository.getTotalGoalHours(),
        subjectRepository.getAllSubjects(),
        sessionsRepository.getTotalSessionsDuration(),
    ){
        state, subjectCount, goalHours, subjects, totalSessionsDuration ->
        state.copy(
            totalSubjectCount= subjectCount,
            totalGoalStudyHours = goalHours,
            subjects = subjects,
            totalStudiedHours = totalSessionsDuration.toHours()
        )
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), DashboardState())


    val tasks : StateFlow<List<Tasks>> = taskRepository.getAllTasks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val recentSessions : StateFlow<List<Sessions>> = sessionsRepository.getRecentFiveSessions().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _snackBarEventFlow = MutableSharedFlow<SnackBarEvent>()
    val snackBarEventFlow = _snackBarEventFlow.asSharedFlow()

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(
                        session = event.session
                    )
                }
            }
            is DashboardEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(
                        goalStudyHours = event.hours
                    )
                }
            }
            is DashboardEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(
                        subjectCardColors = event.color
                    )
                }
            }
            is DashboardEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(
                        subjectName = event.name
                    )
                }
            }
            is DashboardEvent.OnTaskIsCompleteChange -> TODO()
            DashboardEvent.SaveSubject -> saveSubject()
            DashboardEvent.DeleteSession -> TODO()
        }
    }

    private fun saveSubject() {
        viewModelScope.launch {
            try {
                subjectRepository.upsertSubject(
                    subject = Subject(
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _state.update {
                    it.copy(
                        subjectName = "",
                        goalStudyHours = "",
                        subjectCardColors = Subject.subjectCardColors.random()
                    )
                }
            }
            catch (e: Exception) {
                _snackBarEventFlow.emit(
                    SnackBarEvent.ShowSnackBar("Couldn't save subject. ${e.message}",
                        SnackbarDuration.Long))
            }
        }
    }
}