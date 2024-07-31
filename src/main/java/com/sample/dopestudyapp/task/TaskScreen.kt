package com.sample.dopestudyapp.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sample.dopestudyapp.components.DeleteSessionDialog
import com.sample.dopestudyapp.components.SubjectDropDownMenu
import com.sample.dopestudyapp.components.TaskCheckBox
import com.sample.dopestudyapp.components.TaskDatePicker
import com.sample.dopestudyapp.enums.Priority
import com.sample.dopestudyapp.enums.SnackBarEvent
import com.sample.dopestudyapp.enums.changeMillisToDateString
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant

//navigation

//step 1
@Destination /*step 3*/ (navArgsDelegate = TaskScreenArgs::class)
@Composable
fun TaskScreenRoute(
    navigator : DestinationsNavigator
){
    val viewModel : TaskViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    TaskScreen(
        state = state,
        snackbarEvent = viewModel.snackbarEventFlow,
        onEvent = viewModel::onEvent,
        onBackClick = { navigator.navigateUp() }
    )
}

//step 2
data class TaskScreenArgs(
    val subjectId : Int?,
    val taskId : Int?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    state: TaskState,
    snackbarEvent: SharedFlow<SnackBarEvent>,
    onEvent: (TaskEvent) -> Unit,
    onBackClick: () -> Unit
){
    //State for DropDownMenu
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember{ mutableStateOf (false)}
    val scope = rememberCoroutineScope()

    //State for delete dialog
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

    //State for date picker dialog
    var isDatePickerDialogOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    var taskTitleError by rememberSaveable { mutableStateOf<String?>(null) }
    taskTitleError = when {
        state.title.isBlank() -> "Please enter task title."
        state.title.length < 4 -> "Task title is too short."
        state.title.length > 30 -> "Task title is too long."
        else -> null
    }

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


    //Alert dialog for delete onclick
    DeleteSessionDialog(
        isOpen = isDeleteSessionDialogOpen,
        onDismissRequest = {isDeleteSessionDialogOpen = false},
        onConfirmButtonClick = {
            onEvent(TaskEvent.DeleteTask)
            isDeleteSessionDialogOpen = false},
        title = "Delete this Task?",
        bodyText = "Are you sure you want to delete this task? This action cannot be Undone."
    )

    //Dialog for date picker
    TaskDatePicker(
        state = datePickerState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = { isDatePickerDialogOpen = false },
        onConfirmButtonClicked = {
            onEvent(TaskEvent.OnDateChange(millis = datePickerState.selectedDateMillis))
            isDatePickerDialogOpen = false }
    )

    //DropDownMenu Call
    SubjectDropDownMenu(
        sheetState = sheetState ,
        isOpen = isBottomSheetOpen,
        onDismissRequest = { isBottomSheetOpen = false},
        subject = state.subjects ,
        onSubjectClick = {subject ->
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if(!sheetState.isVisible) isBottomSheetOpen = false
            }
            onEvent(TaskEvent.OnRelatedSubjectSelect(subject))
        }
    )

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TaskScreenTopBar(
                onBackClick = onBackClick,
                isTaskExist = state.currentTaskId != null,
                isComplete = state.isTaskComplete,
                CheckBoxBorderColor = state.priority.color,
                onDeleteClick = { isDeleteSessionDialogOpen = true },
                onCheckBoxClick = { onEvent(TaskEvent.OnIsCompleteChange) }
            )
        }
    ){paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal =12.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { onEvent(TaskEvent.OnTitleChange(it)) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = taskTitleError != null && state.title.isNotBlank(),
                supportingText = { Text(text = taskTitleError.orEmpty()) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = { onEvent(TaskEvent.OnDescriptionChange(it)) },
                label = { Text(text = "Description") }
                    )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Due Date",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(datePickerState.selectedDateMillis.changeMillisToDateString(), style = MaterialTheme.typography.bodyLarge)
                IconButton(onClick = { isDatePickerDialogOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Due Date"
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Priority",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Priority.entries.forEach { priority ->
                    PriorityButton(
                        modifier = Modifier.weight(1f),
                        label = priority.title,
                        backgroundColor = priority.color,
                        borderColor = if (priority == Priority.medium) Color.White
                        else Color.Transparent,
                        labelColor = if(priority == Priority.medium) Color.White
                        else Color.White.copy(alpha = .7f),
                        onClick = { onEvent(TaskEvent.OnPriorityChange(priority)) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Related to Subject",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val firstSubject = state.subjects.firstOrNull()?.name ?: ""
                Text(
                    text = state.relatedToSubject ?: firstSubject,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { isBottomSheetOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Subject"
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                enabled = taskTitleError == null,
                onClick = { onEvent(TaskEvent.SaveTask) },
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(text = "Save")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreenTopBar(
    onBackClick: () -> Unit,
    isTaskExist: Boolean,
    isComplete: Boolean,
    CheckBoxBorderColor : Color,
    onDeleteClick: () -> Unit,
    onCheckBoxClick: () -> Unit
    ) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        title = {Text("Task", style = MaterialTheme.typography.headlineSmall)},
        actions = {
                  if(isTaskExist){
                      TaskCheckBox(
                          isCompleted = isComplete,
                          borderColor = CheckBoxBorderColor,
                          onCheckBoxClick = onCheckBoxClick
                      )
                      IconButton(onClick = onDeleteClick) {
                          Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                      }
                  }
                  },
        )
    }

@Composable
fun PriorityButton(
    modifier: Modifier = Modifier,
    label : String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit
){
    Box(
        modifier = modifier.background(color = backgroundColor)
            .clickable { onClick() }
            .padding(5.dp)
            .border(1.dp, color = borderColor, RoundedCornerShape(5.dp))
            .padding(5.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = label, color = labelColor)
    }
}