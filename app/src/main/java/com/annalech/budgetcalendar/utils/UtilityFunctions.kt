package com.annalech.budgetcalendar.utils

import java.text.SimpleDateFormat
import java.util.Calendar

object UtilityFunctions {


    fun dateStringToMillis(dateInString: String):Long{
        val dataFormat =  SimpleDateFormat("dd/MM/yyyy")
        val date = dataFormat.parse(dateInString)
        return date.time
    }

    fun dateMillisToString(dateInMillis:Long):String{
        val dataFormat =  SimpleDateFormat("dd/MM/yyyy")
        val cal = Calendar.getInstance()
        cal.timeInMillis = dateInMillis
        return  dataFormat.format(cal.time)
    }



    fun getEndDate(daysToCount:Int):String{
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR,-daysToCount)
        return dateMillisToString(cal.timeInMillis)
    }
}