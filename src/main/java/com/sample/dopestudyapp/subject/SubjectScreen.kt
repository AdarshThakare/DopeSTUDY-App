package com.sample.dopestudyapp.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sample.dopestudyapp.task.TaskScreenArgs
import com.sample.dopestudyapp.components.AddSubjectDialog
import com.sample.dopestudyapp.components.CountCard
import com.sample.dopestudyapp.components.DeleteSessionDialog
import com.sample.dopestudyapp.components.studySessionsList
import com.sample.dopestudyapp.components.taskList
import com.sample.dopestudyapp.destinations.TaskScreenRouteDestination
import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.enums.SnackBarEvent
import com.sample.dopestudyapp.sessions
import com.sample.dopestudyapp.tasks
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

//navigation
//step 1
@Destination /* Step 3 */ (navArgsDelegate = SubjectScreenArgs::class)
@Composable
fun SubjectScreenRoute(
    //step 4
    navigator : DestinationsNavigator
){
    val viewModel : SubjectViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    SubjectScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarEvent = viewModel.snackbarEventFlow,
        //step 5
        onBackClick = {navigator.navigateUp()},
        onTaskCardClick = {taskId ->
            val NavArgs = TaskScreenArgs(taskId = taskId, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(NavArgs))
                          },
        onAddTasksClick = {
            val NavArgs = TaskScreenArgs(taskId = null, subjectId = -1)
            navigator.navigate(TaskScreenRouteDestination(NavArgs))
        }
    )
}

//step 2
data class SubjectScreenArgs(
    val subjectId : Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    state: SubjectState,
    onEvent: (SubjectEvent) -> Unit,
    snackbarEvent: SharedFlow<SnackBarEvent>,
    onBackClick: () -> Unit,
    onTaskCardClick : (Int?) -> Unit,
    onAddTasksClick : () -> Unit,
){

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFABExtended by remember { derivedStateOf{ listState.firstVisibleItemIndex == 0 } }


    var isEditSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable {mutableStateOf(false) }
    var isDeleteSubjectDialogOpen by rememberSaveable {mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when (event) {
                is SnackBarEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackBarEvent.NavigateUp -> {
                    onBackClick()
                }
            }
        }
    }

    LaunchedEffect(key1 = state.studiedHours, key2 = state.goalStudyHours) {
        onEvent(SubjectEvent.UpdateProgress)
    }

    /*Add Subject Dialog*/
    AddSubjectDialog(
        isOpen = isEditSubjectDialogOpen,
        onDismissRequest = {isEditSubjectDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(SubjectEvent.UpdateSubject)
            isEditSubjectDialogOpen = false},
        subjectName = state.subjectName ,
        subjectHours = state.goalStudyHours ,
        selectedColors = state.subjectCardColors ,
        onSubjectNameChange = { onEvent(SubjectEvent.OnSubjectNameChange(it)) },
        onSubjectHoursChange = { onEvent(SubjectEvent.OnGoalStudyHoursChange(it)) },
        onColorChange = { onEvent(SubjectEvent.OnSubjectCardColorChange(it)) },
    )

    DeleteSessionDialog(
        isOpen = isDeleteSubjectDialogOpen,
        onDismissRequest = {isDeleteSubjectDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(SubjectEvent.DeleteSubject)
            isDeleteSubjectDialogOpen = false},
        title = "Delete Subject?",
        bodyText = "Are you sure you want to delete this subject?\nAll related tasks ans study sessions will\nalso be deleted This action cannot be Undone."
    )


    DeleteSessionDialog(
        isOpen = isDeleteSessionDialogOpen,
        onDismissRequest = {isDeleteSessionDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(SubjectEvent.DeleteSession)
            isDeleteSessionDialogOpen = false},
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? This action cannot be Undone."
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SubjectScreenTopBar(
            title = state.subjectName,
            onBackClick = onBackClick,
            onDeleteClick = { isDeleteSubjectDialogOpen = true},
            onEditClick = { isEditSubjectDialogOpen = true},
            scrollBehavior = scrollBehavior
        ) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTasksClick,
                icon = {Icon( imageVector = Icons.Default.Add, contentDescription = "Add")},
                text = { Text(text = "Add Task")},
                expanded = isFABExtended,
            )
        }
    ){paddingValues ->
       LazyColumn (
           state = listState,
           modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues)
       ){
           item{
               SubjectOverviewSection(
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(12.dp),
                   studiedHours = state.studiedHours.toString(),
                   goalHours = state.goalStudyHours,
                   progress = state.progress
               )
           }
           taskList(
               sectionTitle ="UPCOMING TASKS",
               emptyListText = "You don't have any upcoming tasks.\nClick the + button in the Subjects panel\nto add new tasks. ",
               tasks = state.upcomingTasks,
               onCheckBoxClick = { onEvent(SubjectEvent.OnTaskIsCompleteChange(it)) },
               onTaskCardClick = onTaskCardClick
           )
           item{
               Spacer(modifier = Modifier.height(16.dp))
           }
           taskList(
               sectionTitle ="COMPLETED TASKS",
               emptyListText = "You don't have any completed tasks.\n Activate the Checkbox to view your\n completed tasks. ",
               tasks = state.completedTasks,
               onCheckBoxClick = { onEvent(SubjectEvent.OnTaskIsCompleteChange(it)) },
               onTaskCardClick = onTaskCardClick
           )
           item{
               Spacer(modifier = Modifier.height(16.dp))
           }
           studySessionsList("You don't have any upcoming study sessions yet.\nStart a study session to begin recording your progress.",
               "RECENT STUDY SESSIONS",
               sessions = state.recentSessions,
               onDeletionOnClick = { isDeleteSessionDialogOpen = true
                   onEvent(SubjectEvent.OnDeleteSessionButtonClick(it))}
           )
       }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreenTopBar(
    title : String,
    onBackClick : () -> Unit,
    onDeleteClick : () -> Unit,
    onEditClick : () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackClick ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        },
        title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineMedium,) },
        actions = {
            IconButton(onClick = onDeleteClick ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
        }
    )
}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHours : String,
    goalHours : String,
    progress : Float
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        var percentageProgress = remember(progress) {
            (progress*100).toInt().coerceIn(0,100)
        }
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier.height(75.dp).width(75.dp),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = progress,
                strokeWidth = 6.dp,
                strokeCap = StrokeCap.Round,
            )
            Text(text = "$percentageProgress%")
        }
    }
}