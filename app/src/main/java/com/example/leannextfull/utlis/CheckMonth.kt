package com.example.leannextfull.utlis

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date
import java.util.Locale

import kotlin.time.Duration.Companion.days

object CheckMonth {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentMonthStartDate(previousMonth: Int):Date {
        Log.d("TAGW", previousMonth.toLong().toString())
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val firstDateTimeOfCurrentMonth = TemporalAdjusters.firstDayOfMonth()
            .adjustInto(LocalDate.now().plusMonths(previousMonth.toLong()))
        Log.d("TAG", firstDateTimeOfCurrentMonth.toString())
        return firstDateTimeOfCurrentMonth.let {
            format.parse(it.toString())
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentMonthEndDate(previousMonth: Int):Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val lastDateTimeOfCurrentMonth = TemporalAdjusters.lastDayOfMonth()
            .adjustInto(LocalDate.now().plusMonths(previousMonth.toLong()))
        Log.d("TAG", lastDateTimeOfCurrentMonth.toString())
        return lastDateTimeOfCurrentMonth.let {
            format.parse(it.toString())
        }
    }


}