package com.sample.dopestudyapp.database

import androidx.room.TypeConverter

class ColorListEnabler {

    @TypeConverter
    fun fromColorList(colorList : List<Int>) :  String{
        return colorList.joinToString(","){it.toString()}
    }

    @TypeConverter
    fun toColorList(colorListString : String) : List<Int>{
        return colorListString.split(",").map{ it.toInt() }
    }
}