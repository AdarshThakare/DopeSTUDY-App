package com.sample.dopestudyapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sample.dopestudyapp.R

@Composable
fun SubjectCard(
    modifier : Modifier = Modifier,
    gradientColor : List<Color>,
    onClick : () -> Unit,
    subjectName : String
) {
    Box(modifier = modifier.height(150.dp)
        .clickable { onClick() }
        .background(
            brush = Brush.verticalGradient(gradientColor),
            shape = MaterialTheme.shapes.medium
        )) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal =30.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center){
            Image(painter = painterResource(id = R.drawable.book_100),
                contentDescription = "Subject Card Image",
                modifier = Modifier.height(80.dp).align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop)
            Text(
                color = Color.White,
                text = subjectName,
                maxLines = 1,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
    }
}