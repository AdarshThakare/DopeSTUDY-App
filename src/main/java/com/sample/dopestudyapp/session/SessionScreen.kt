package com.sample.dopestudyapp.session
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sample.dopestudyapp.components.DeleteSessionDialog
import com.sample.dopestudyapp.components.SubjectDropDownMenu
import com.sample.dopestudyapp.components.studySessionsList
import com.sample.dopestudyapp.sessions
import com.sample.dopestudyapp.subjects
import kotlinx.coroutines.launch

//navigation

//step 1
@Destination
@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator
){
    val viewModel : SessionViewModel = hiltViewModel()
    SessionScreen(
        onBackButtonClick = {navigator.navigateUp()}
   )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    onBackButtonClick : () -> Unit
){

    //State for DropDownMenu
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by remember{ mutableStateOf (false)}
    val scope = rememberCoroutineScope()

    //State for delete dialog
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

    //DropDownMenu Call
    SubjectDropDownMenu(
        sheetState = sheetState ,
        isOpen = isBottomSheetOpen,
        onDismissRequest = { isBottomSheetOpen = false},
        subject = subjects ,
        onSubjectClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if(!sheetState.isVisible) isBottomSheetOpen = false
            }
        }
    )

    DeleteSessionDialog(
        isOpen = isDeleteSessionDialogOpen,
        onDismissRequest = {isDeleteSessionDialogOpen = false},
        onConfirmButtonClick = {isDeleteSessionDialogOpen = false},
        title = "Delete this Session?",
        bodyText = "Are you sure you want to delete this session? This action cannot be Undone."
    )

    Scaffold(
        topBar = {
            SessionScreenTopBar(
                onBackButtonClick = onBackButtonClick
            )
        }
    ) {paddingValues ->
        LazyColumn (
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ){
            item {
                TimerSection(
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            item{
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                RelatedToSubjectSection(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    relatedToSubject = "English",
                    selectSubjectButtonClick = { isBottomSheetOpen = true }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            item{
                ButtonSection(
                    modifier = Modifier.fillMaxWidth(),
                    startButtonClick = { /*TODO*/ },
                    cancelButtonClick = { /*TODO*/ },
                    finishButtonClick = { /*TODO*/ }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionsList("You don't have any upcoming study sessions yet.",
                "STUDY SESSIONS HISTORY",
                sessions = sessions,
                onDeletionOnClick = { isDeleteSessionDialogOpen = true }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreenTopBar(
    onBackButtonClick : () -> Unit
){
    TopAppBar(
        navigationIcon = {
                         IconButton(onClick = onBackButtonClick) {
                             Icon(imageVector = Icons.Default.ArrowBack,
                                     contentDescription = "Back")
                         }
        },
        title ={
            Text(text = "Study Sessions", style = MaterialTheme.typography.headlineSmall)
        },
    )
}

@Composable
fun TimerSection(modifier : Modifier){
    Box(modifier = modifier,
        contentAlignment = Alignment.Center){
        Box(modifier = Modifier.height(250.dp).width(250.dp)
            .border(width = 5.dp, color = MaterialTheme.colorScheme.surfaceVariant, CircleShape))
        Text(text = "00:05:32", style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp))
    }
}

@Composable
fun RelatedToSubjectSection(
    modifier: Modifier,
    relatedToSubject : String,
    selectSubjectButtonClick : () -> Unit

){
    Column(modifier = modifier) {
        Text(
            text = "Related to Subject",
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(relatedToSubject, style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = selectSubjectButtonClick) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Subject"
                )
            }
        }
    }
}

@Composable
private fun ButtonSection(
    modifier: Modifier,
    startButtonClick : () -> Unit,
    cancelButtonClick : () -> Unit,
    finishButtonClick : () -> Unit
){
    Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.SpaceAround
    ){
        Button(onClick = startButtonClick) {
            Text(text = "Start",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp))
        }
        Button(onClick = cancelButtonClick) {
            Text(text = "Cancel",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp))
        }
        Button(onClick = finishButtonClick) {
            Text(text = "Finish",
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp))
        }
    }
}