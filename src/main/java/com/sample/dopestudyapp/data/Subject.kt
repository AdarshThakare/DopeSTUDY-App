package com.sample.dopestudyapp.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sample.dopestudyapp.ui.theme.gradient1
import com.sample.dopestudyapp.ui.theme.gradient2
import com.sample.dopestudyapp.ui.theme.gradient3
import com.sample.dopestudyapp.ui.theme.gradient4
import com.sample.dopestudyapp.ui.theme.gradient5

@Entity
data class Subject(
    val name: String,
    val goalHours: Float,
    val colors: List<Int>,
    @PrimaryKey( autoGenerate = true)
    val subjectId : Int? = null
){
    companion object{
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
