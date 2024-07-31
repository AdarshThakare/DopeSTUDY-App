package com.sample.dopestudyapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sessions(
    val sessionSubjectId : Int,
    val relatedToSubjects : String,
    val date : Long,
    val duration : Long,
    @PrimaryKey(autoGenerate = true)
    val sessionId : Int? = null
)
