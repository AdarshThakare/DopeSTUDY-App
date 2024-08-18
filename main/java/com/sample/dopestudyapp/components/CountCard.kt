package com.sample.dopestudyapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sample.dopestudyapp.R
import com.sample.dopestudyapp.data.Subject

@Composable
fun CountCard(
    modifier: Modifier = Modifier,
    count: String,
    headingText : String,
){
    ElevatedCard(modifier = modifier){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = headingText,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = count,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 30.sp)
            )
        }
    }
}

@Composable
fun CountCardSection(modifier : Modifier,
                             subjectCount : Int,
                             studiedHours : String,
                             goalStudyHours : String){
    Row(modifier = modifier){
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = subjectCount.toString()
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Hours",
            count = goalStudyHours
        )
    }

}

@Composable
fun SubjectCardSection(
    modifier: Modifier,
    subjectList : List<Subject>,
    emptyListText : String =  "No Subject Added have been added yet.\nClick the + button to add a new Subject.",
    onAddItemClicked : () -> Unit,
    onSubjectCardClick : (Int?) -> Unit
){
    Column(modifier = modifier){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text= "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(
              onClick = onAddItemClicked
            ) {
                 Icon(imageVector = Icons.Filled.Add,
                     contentDescription = "Add Subject")
             }
        }
        if(subjectList.isEmpty()){
            Image(painter = painterResource(id = R.drawable.icons8_book_shelf_96),
                contentDescription = "Subject List Empty",
                modifier = Modifier.height(120.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ){
            items(subjectList){
                Subject ->
                SubjectCard(subjectName = Subject.name,
                    gradientColor = Subject.colors.map{ Color(it) },
                    onClick = {onSubjectCardClick(Subject.subjectId)}
                )
            }
        }
    }
}