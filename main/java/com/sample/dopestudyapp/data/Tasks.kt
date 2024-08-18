package com.sample.dopestudyapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tasks(
    val title : String,
    val description : String,
    val relatedToSubject : String,
    val dueDate : Long,
    val taskSubjectId : Int,
    val isCompleted : Boolean,
    val priority : Int,
    @PrimaryKey(autoGenerate = true)
    val taskId : Int? = null
)
