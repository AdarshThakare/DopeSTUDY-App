package com.sample.dopestudyapp.task

import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.enums.Priority

data class TaskState (
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,
    val isTaskComplete: Boolean = false,
    val priority: Priority = Priority.low,
    val relatedToSubject: String? = null,
    val subjects: List<Subject> = emptyList(),
    val subjectId: Int? = null,
    val currentTaskId: Int? = null
)