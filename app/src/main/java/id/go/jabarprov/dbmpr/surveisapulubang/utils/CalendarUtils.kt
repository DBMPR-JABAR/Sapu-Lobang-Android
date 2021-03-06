package id.go.jabarprov.dbmpr.surveisapulubang.utils

import java.text.SimpleDateFormat
import java.util.*

abstract class CalendarUtils {
    companion object {
        fun formatCalendarToString(calendar: Calendar, pattern: String = "yyyy-MM-dd"): String {
            val format = SimpleDateFormat(pattern, Locale("en", "US"))
            return format.format(calendar.timeInMillis)
        }

        fun formatStringToCalendar(string: String?): Calendar {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale("en", "US"))
            return Calendar.getInstance().apply {
                time = string?.let { format.parse(it) } ?: Date()
            }
        }

        fun formatTimeInMilliesToCalendar(timeInMillies: Long): Calendar {
            return Calendar.getInstance().apply {
                timeInMillis = timeInMillies
            }
        }
    }
}