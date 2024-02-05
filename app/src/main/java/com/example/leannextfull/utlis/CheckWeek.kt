package com.example.leannextfull.utlis

import java.util.Calendar
import java.util.Date

/**Метод для поиска начала и конца недели*/
object CheckWeek {
    fun PreviousNextWeekModay(previousWeek: Int): Date {
        return Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(
                Calendar.WEEK_OF_YEAR,
                Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + previousWeek
            )
            set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }.time
    }

    fun PreviousNextWeekSunday(previousWeek: Int): Date {
        return Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.MONDAY
            set(
                Calendar.WEEK_OF_YEAR,
                Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + previousWeek
            )
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }.time
    }
}