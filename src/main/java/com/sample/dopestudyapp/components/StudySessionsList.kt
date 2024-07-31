package com.sample.dopestudyapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sample.dopestudyapp.R
import com.sample.dopestudyapp.data.Sessions

fun LazyListScope.studySessionsList(
    emptyListText : String,
    sectionTitle : String,
    sessions : List<Sessions>,
    onDeletionOnClick: (Sessions) -> Unit
){
    item {
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            text = sectionTitle,
            style = MaterialTheme.typography.bodySmall,
        )
    }
    if(sessions.isEmpty()){
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.table),
                    contentDescription = "Tasks List Empty",
                    modifier = Modifier.height(150.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = emptyListText,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
                Spacer(Modifier.height(30.dp))

            }
        }
    }
    items(sessions){ Sessions ->
        StudySessionsCard(sessions = Sessions,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
            onDeletionOnClick = {onDeletionOnClick(Sessions)})
    }
}

@Composable
private fun StudySessionsCard(
    modifier : Modifier = Modifier,
    sessions : Sessions,
    onDeletionOnClick : () -> Unit
){
    Card(
        modifier = modifier
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(start = 12.dp)){
                Text(
                    text = sessions.relatedToSubjects,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${sessions.date}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${sessions.duration} hr",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = onDeletionOnClick ){
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                )
            }
        }
    }
}