package com.sample.dopestudyapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.sample.dopestudyapp.data.Sessions
import com.sample.dopestudyapp.data.Subject
import com.sample.dopestudyapp.data.Tasks
import com.sample.dopestudyapp.destinations.SessionScreenRouteDestination
import com.sample.dopestudyapp.session.StudySessionTimerService
import com.sample.dopestudyapp.ui.theme.DopeStudyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    lateinit var timerService: StudySessionTimerService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as StudySessionTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
         }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StudySessionTimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (isBound) {
                DopeStudyAppTheme {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            dependency(SessionScreenRouteDestination) { timerService }
                        }
                    )
                }
            }
        }
        requestPermission()
    }
    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
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