package com.sample.dopestudyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sample.dopestudyapp.data.Subject

@Composable
fun AddSubjectDialog(
    isOpen : Boolean,
    title : String = "Add/Update Subject",
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    selectedColors : List<Color>,
    onColorChange : (List<Color>) -> Unit,
    subjectName : String,
    subjectHours : String,
    onSubjectNameChange : (String) -> Unit,
    onSubjectHoursChange : (String) -> Unit
){
    /*Throw Errors*/
    var subjectNameError by rememberSaveable{ mutableStateOf<String?>(null) }
    var subjectHoursError by rememberSaveable{ mutableStateOf<String?>(null) }

    subjectNameError = when {
        subjectName.isBlank() -> "Please Enter the subject names"
        subjectName.length > 20 -> "Subject name is too long."
        subjectName.length < 2 -> "Subject name is too short."
        else -> null
    }

    subjectHoursError = when {
        subjectHours.isBlank() -> "Please Enter the study hours"
        subjectHours.toFloatOrNull() == null -> "Invalid number entered."
        subjectHours.toFloat() < 0 -> "Study hours cannot be negative."
        subjectHours.toFloat() < 1f -> "Please enter a value at least or more than 1 hour."
        subjectHours.toFloat() > 100f -> "Study hours cannot be set more than 100 hours."
        else -> null
    }


    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title) },
            text = {
                   Column(){
                      Row(
                          modifier = Modifier.fillMaxWidth()
                              .padding(bottom = 16.dp),
                          horizontalArrangement = Arrangement.SpaceAround
                      ){
                          Subject.subjectCardColors.forEach {
                              colors ->
                              Box(modifier = Modifier
                                  .size(24.dp)
                                  .clip(CircleShape)
                                  .border(
                                      width = 2.dp,
                                      color = if(colors == selectedColors)      Color.Black
                                      else                                      Color.Transparent
                                      , shape = CircleShape)
                                  .background(brush = Brush.verticalGradient(colors))
                                  .clickable{onColorChange(colors)}
                                  )
                              }
                          }
                       OutlinedTextField(
                           value = subjectName,
                           onValueChange = onSubjectNameChange,
                           label = { Text(text = "Subject Name") },
                           singleLine = true,
                           isError = subjectNameError!=null && subjectName.isNotBlank(),
                           supportingText = { Text(text = subjectNameError.orEmpty()) }
                       )
                       Spacer(modifier = Modifier.height(16.dp))
                       OutlinedTextField(
                           value = subjectHours,
                           onValueChange = onSubjectHoursChange,
                           label = { Text(text = "Get Study Hours")},
                           singleLine = true,
                           keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                           isError = subjectHoursError != null && subjectHours.isNotBlank(),
                           supportingText = {Text(text = subjectHoursError.orEmpty())}
                       )
                       }
                   },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = onConfirmButtonClick,
                    enabled = subjectNameError == null && subjectHoursError == null) {
                    Text(text = "Save")
                }
            }
        )
    }
}