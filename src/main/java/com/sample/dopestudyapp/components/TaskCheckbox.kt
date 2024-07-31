package com.sample.dopestudyapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun TaskCheckBox(
    isCompleted : Boolean,
    borderColor : Color,
    onCheckBoxClick : () -> Unit
){
    Box(modifier = Modifier.clickable(onClick = onCheckBoxClick)
        .height(25.dp)
        .width(25.dp)
        .clip(CircleShape)
        .border(2.dp, borderColor, CircleShape),
    contentAlignment = Alignment.Center){
        AnimatedVisibility(visible = isCompleted){
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                modifier = Modifier.height(20.dp).width(20.dp),
            )
        }
    }
}