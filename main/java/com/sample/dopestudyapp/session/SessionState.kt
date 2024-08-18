package com.sample.dopestudyapp.session

import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Subject

data class SessionState(
        val subjects: List<Subject> = emptyList(),
        val sessions: List<Sessions> = emptyList(),
        val relatedToSubject: String? = null,
        val subjectId: Int? = null,
        val session: Sessions? = null
)