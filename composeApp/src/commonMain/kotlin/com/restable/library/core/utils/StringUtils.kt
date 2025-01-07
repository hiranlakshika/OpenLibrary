package com.restable.library.core.utils

import kotlin.math.round

object StringUtils {
    fun getRoundNumberText(number: Double): String = (round(number * 10) / 10.0).toString()
}