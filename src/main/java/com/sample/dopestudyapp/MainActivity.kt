package com.sample.dopestudyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.ramcosta.composedestinations.DestinationsNavHost
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.data.Tasks
import com.sample.dopestudyapp.ui.theme.DopeStudyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DopeStudyAppTheme {
               DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

val subjects =  listOf(
    Subject("DELD",10f, Subject.subjectCardColors[0].map{it.toArgb()},0),
    Subject("DM",10f, Subject.subjectCardColors[1].map{it.toArgb()},0),
    Subject("CG",10f, Subject.subjectCardColors[2].map{it.toArgb()},0),
    Subject("OOPS",10f, Subject.subjectCardColors[3].map{it.toArgb()},0),
    Subject("FDS",10f, Subject.subjectCardColors[4].map{it.toArgb()},0)
)

val tasks = listOf(
    Tasks("Prepare for test","","DELD",0L, 1,false,1,0),
    Tasks("Doubts Clearing in Coaching blah blah blah blah","","CG",0L, 2,false,2,0),
    Tasks("Assignment for Practice","","FDS",0L, 3,false,0,0),
    Tasks("Plan for Revision","","OOPS",0L, 4,false,2,0),
    Tasks("Do Homework","","DELD",0L, 5,true,1,0)
)

val sessions = listOf(
    Sessions(relatedToSubjects = "Digital Electronics",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0),
    Sessions(relatedToSubjects = "Obj Oriented Program",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0),
    Sessions(relatedToSubjects = "Comp Graphics",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0),
    Sessions(relatedToSubjects = "Disc Mathematics",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0),
    Sessions(relatedToSubjects = "Fund of Data Structures",
        date = 0L,
        duration = 2,
        sessionSubjectId = 0,
        sessionId = 0),
)