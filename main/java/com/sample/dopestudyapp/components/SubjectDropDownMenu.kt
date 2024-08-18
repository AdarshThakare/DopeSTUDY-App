package com.sample.dopestudyapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sample.dopestudyapp.data.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDropDownMenu(
    sheetState : SheetState,
    isOpen : Boolean,
    subject : List<Subject>,
    bottomSheetTitle :  String = "Related to Subject",
    onDismissRequest : () -> Unit,
    onSubjectClick : (Subject) -> Unit
){
    if(isOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(text = bottomSheetTitle)
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp)
            ) {
                items(subject) { subject ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSubjectClick(subject) }
                            .padding(8.dp)
                    ) {
                        Text(text = subject.name)
                    }
                }
                if (subject.isEmpty()) {
                    item {
                        Text(
                            text = "No Subject Found.\nReady to begin? First, add a subject.",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}