package com.sample.dopestudyapp.dashboard

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sample.dopestudyapp.subject.SubjectScreenArgs
import com.sample.dopestudyapp.task.TaskScreenArgs
import com.sample.dopestudyapp.components.AddSubjectDialog
import com.sample.dopestudyapp.components.CountCardSection
import com.sample.dopestudyapp.components.DeleteSessionDialog
import com.sample.dopestudyapp.components.SubjectCardSection
import com.sample.dopestudyapp.components.studySessionsList
import com.sample.dopestudyapp.components.taskList
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.destinations.SessionScreenRouteDestination
import com.sample.dopestudyapp.destinations.SubjectScreenRouteDestination
import com.sample.dopestudyapp.destinations.TaskScreenRouteDestination
import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.data.Tasks
import com.sample.dopestudyapp.enums.SnackBarEvent
import com.sample.dopestudyapp.sessions
import com.sample.dopestudyapp.subjects
import com.sample.dopestudyapp.tasks
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

//navigation
//step 1
@Destination(start = true)
@Composable
fun DashboardScreenRoute(
    //step 4
    navigator : DestinationsNavigator
){
    val viewModel : DashboardViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val recentSessions by viewModel.recentSessions.collectAsStateWithLifecycle()

    DashboardScreen(
        //step 5
        state = state,
        tasks = tasks,
        recentSessions = recentSessions,
        onEvent = viewModel::onEvent,
        snackBarEvent = viewModel.snackBarEventFlow,
        onSubjectCardOnClick = {subjectId ->
            subjectId?.let {
                val NavArgs = SubjectScreenArgs(subjectId)
                navigator.navigate(SubjectScreenRouteDestination(NavArgs))
            }
        },
        onTaskCardClick = {taskId ->
            val NavArgs = TaskScreenArgs(taskId = taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(NavArgs))
        },
        onStudySessionButtonClick = {
            navigator.navigate(SessionScreenRouteDestination())
        }
    )
}

@Composable
private fun DashboardScreen(
    state : DashboardState,
    tasks : List<Tasks>,
    recentSessions : List<Sessions>,
    onEvent : (DashboardEvent) -> Unit,
    snackBarEvent: SharedFlow<SnackBarEvent>,
    onSubjectCardOnClick : (Int?) -> Unit,
    onTaskCardClick : (Int?) -> Unit,
    onStudySessionButtonClick : () -> Unit
) {
    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable {mutableStateOf(false) }

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true){
        snackBarEvent.collectLatest {event ->
            when(event){
                is SnackBarEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }
                SnackBarEvent.NavigateUp -> {}
            }
        }
    }

    /*Add Subject Dialog*/
    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        onDismissRequest = {isAddSubjectDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(DashboardEvent.SaveSubject)
            isAddSubjectDialogOpen = false
                               },
        subjectName = state.subjectName ,
        subjectHours = state.goalStudyHours ,
        selectedColors = state.subjectCardColors ,
        onSubjectNameChange = { onEvent(DashboardEvent.OnSubjectNameChange(it))},
        onSubjectHoursChange = { onEvent(DashboardEvent.OnGoalStudyHoursChange(it)) },
        onColorChange = { onEvent(DashboardEvent.OnSubjectCardColorChange(it)) },
    )

    DeleteSessionDialog(
        isOpen = isDeleteSessionDialogOpen,
        onDismissRequest = {isDeleteSessionDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(DashboardEvent.DeleteSession)
            isDeleteSessionDialogOpen = false},
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? This action cannot be Undone."
    )
    /*Main Content Starts from here*/
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackBarHostState)},
        topBar ={ DashboardTopBar() }) {
            paddingValues ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ){
                item {
                    CountCardSection(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        subjectCount = state.totalSubjectCount,
                        studiedHours = state.totalStudiedHours.toString(),
                        goalStudyHours = state.totalGoalStudyHours.toString()
                    )
                }
                item {
                    SubjectCardSection(
                        modifier = Modifier.fillMaxWidth(),
                        subjectList = state.subjects,
                        onAddItemClicked = { isAddSubjectDialogOpen = true },
                        onSubjectCardClick = onSubjectCardOnClick
                    )
                }
                item{
                    Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp, vertical = 20.dp),onClick = onStudySessionButtonClick){
                        Text(text = "Start Study Sessions")
                    }
                }
                taskList(
                    sectionTitle ="UPCOMING TASKS",
                    emptyListText = "You don't have any upcoming tasks.\nClick the + button in the Subjects panel\nto add new tasks. ",
                    tasks = tasks,
                    onCheckBoxClick = {onEvent(DashboardEvent.OnTaskIsCompleteChange(it))},
                    onTaskCardClick = onTaskCardClick
                )
                item{
                    Spacer(modifier = Modifier.height(16.dp))
                }
                studySessionsList("You don't have any upcoming study sessions yet.",
                    "RECENT STUDY SESSIONS",
                    sessions = recentSessions,
                    onDeletionOnClick = {
                        onEvent(DashboardEvent.OnDeleteSessionButtonClick(it))
                        isDeleteSessionDialogOpen = true }
                )
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "D O P E   S T U D Y",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}