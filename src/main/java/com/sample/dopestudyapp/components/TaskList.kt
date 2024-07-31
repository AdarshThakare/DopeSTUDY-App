package com.sample.dopestudyapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sample.dopestudyapp.R
import com.sample.dopestudyapp.enums.Priority
import com.sample.dopestudyapp.data.Tasks

fun LazyListScope.taskList(
    emptyListText : String,
    sectionTitle : String,
    tasks : List<Tasks>,
    onTaskCardClick : (Int?) -> Unit,
    onCheckBoxClick : (Tasks) -> Unit
){
    item {
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            text = sectionTitle,
            style = MaterialTheme.typography.bodySmall,
        )
    }
    if(tasks.isEmpty()){
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icons8_survey_100),
                    contentDescription = "Tasks List Empty",
                    modifier = Modifier.height(120.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = emptyListText,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }
    }
    items(tasks){ Tasks ->
        TaskCard(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            task = Tasks,
            onCheckBoxClick = { onCheckBoxClick(Tasks) },
            onClick = { onTaskCardClick(Tasks.taskId) }
        )
    }
}

@Composable
private fun TaskCard(
    modifier : Modifier = Modifier,
    task : Tasks,
    onCheckBoxClick : () -> Unit,
    onClick: () -> Unit
){
    ElevatedCard(
        modifier = modifier.clickable{ onClick() }
    ){
        Row(modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
                TaskCheckBox(
                    task.isCompleted,
                    Priority.fromInt(task.priority).color,
                    onCheckBoxClick,
                )
                Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)){
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if(task.isCompleted){
                        TextDecoration.LineThrough
                    }else{
                        TextDecoration.None
                    }
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${task.dueDate}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}