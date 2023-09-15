package com.ale.vncs.codfy.utils

import java.math.RoundingMode
import java.text.DecimalFormat

object ConvertUtil {
    fun convertTime(duration: Int): String {
        val df = DecimalFormat("##")
        df.roundingMode = RoundingMode.DOWN

        val hours = df.format(duration / 1000 / 3600).toInt()
        val minutes = df.format(duration / 1000 / 60 % 60).toInt()
        val seconds = df.format(duration / 1000 % 60).toInt()


        val displayMinute = minutes.toString()
        val displaySecond = df.format(seconds).padStart(2, '0')

        if (hours > 0) {
            return "$hours:${displayMinute.padStart(2, '0')}:$displaySecond"
        }
        return "$displayMinute:$displaySecond"
    }
}
