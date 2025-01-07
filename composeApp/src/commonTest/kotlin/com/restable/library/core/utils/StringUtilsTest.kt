package com.restable.library.core.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class StringUtilsTest {

    @Test
    fun getRoundNumberText_roundsToOneDecimalPlace() {
        val number = 3.14159
        val expected = "3.1"
        val actual = StringUtils.getRoundNumberText(number)
        assertEquals(expected, actual)
    }

    @Test
    fun getRoundNumberText_handlesWholeNumber() {
        val number = 5.0
        val expected = "5.0"
        val actual = StringUtils.getRoundNumberText(number)
        assertEquals(expected, actual)
    }

    @Test
    fun getRoundNumberText_handlesTrailingZeros() {
        val number = 2.50
        val expected = "2.5"
        val actual = StringUtils.getRoundNumberText(number)
        assertEquals(expected, actual)
    }

    @Test
    fun getRoundNumberText_handlesNegativeNumber() {
        val number = -1.234
        val expected = "-1.2"
        val actual = StringUtils.getRoundNumberText(number)
        assertEquals(expected, actual)
    }
}