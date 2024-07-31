package com.sample.dopestudyapp.enums

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import com.sample.dopestudyapp.ui.theme.Green
import com.sample.dopestudyapp.ui.theme.Orange
import com.sample.dopestudyapp.ui.theme.Red
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class Priority (val title: String,val color : Color,  val value : Int){
    low("Low",Green,0),
    medium("Medium",Orange, 1),
    high("High", Red, 2);

    companion object{
        fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: medium
    }
}


//This function converts the date value into string format
fun Long?.changeMillisToDateString(): String {
    val date : LocalDate = this?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } ?: LocalDate.now()
    return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}

fun Long.toHours() : Float{
    val hours = this.toFloat() / 3600f
    return "%.2f".format(hours).toFloat()
}

sealed class SnackBarEvent{

    data class ShowSnackBar(
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        ) : SnackBarEvent()

    data object NavigateUp: SnackBarEvent()
}