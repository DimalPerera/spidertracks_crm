package com.spidertracks.crm.util

import java.text.SimpleDateFormat
import java.util.*

class DateTimeController {

    companion object {
        fun convertIsoToCustomFormat(isoDate: String?, customFormat: String): String {

            if(isoDate == null) {
                return ""
            }
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val customFormat = SimpleDateFormat(customFormat, Locale.getDefault())
                val date = isoFormat.parse(isoDate)
                return customFormat.format(date)

        }

        fun convertCalenderToIsoFormat(isoDate: Calendar?): String {

            val calendar = Calendar.getInstance()
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
            val isoDate = isoFormat.format(calendar.time)

            return isoDate
        }
    }
}