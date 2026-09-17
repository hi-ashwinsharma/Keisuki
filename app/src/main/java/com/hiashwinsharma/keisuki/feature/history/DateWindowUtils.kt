package com.hiashwinsharma.keisuki.feature.history

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class TimeWindowConfig(
    val startTime: Long,
    val endTime: Long,
    val label: String,
    val mode: DateRangeMode,
    val anchor: Long
)

object DateWindowUtils {

    fun shiftPeriod(mode: DateRangeMode, anchor: Long, amount: Int): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = anchor }
        when (mode) {
            DateRangeMode.DAY -> cal.add(Calendar.DAY_OF_YEAR, amount)
            DateRangeMode.WEEK -> cal.add(Calendar.WEEK_OF_YEAR, amount)
            DateRangeMode.MONTH -> cal.add(Calendar.MONTH, amount)
        }
        return cal.timeInMillis
    }

    fun calculateWindow(mode: DateRangeMode, anchor: Long): TimeWindowConfig {
        val cal = Calendar.getInstance().apply { timeInMillis = anchor }
        val nowCal = Calendar.getInstance()

        return when (mode) {
            DateRangeMode.DAY -> {
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.timeInMillis

                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                cal.set(Calendar.MILLISECOND, 999)
                val end = cal.timeInMillis

                val isToday = isSameDay(cal, nowCal)
                val yesterdayCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
                val isYesterday = isSameDay(cal, yesterdayCal)

                val label = when {
                    isToday -> "Today, " + SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(anchor))
                    isYesterday -> "Yesterday, " + SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(anchor))
                    else -> SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(Date(anchor))
                }
                TimeWindowConfig(start, end, label, mode, anchor)
            }
            DateRangeMode.WEEK -> {
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.timeInMillis

                cal.add(Calendar.DAY_OF_WEEK, 6)
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                cal.set(Calendar.MILLISECOND, 999)
                val end = cal.timeInMillis

                val fmt = SimpleDateFormat("MMM d", Locale.getDefault())
                val label = "${fmt.format(Date(start))} – ${fmt.format(Date(end))}"
                TimeWindowConfig(start, end, label, mode, anchor)
            }
            DateRangeMode.MONTH -> {
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                val start = cal.timeInMillis

                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                cal.set(Calendar.MILLISECOND, 999)
                val end = cal.timeInMillis

                val label = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(start))
                TimeWindowConfig(start, end, label, mode, anchor)
            }
        }
    }

    private fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }
}
