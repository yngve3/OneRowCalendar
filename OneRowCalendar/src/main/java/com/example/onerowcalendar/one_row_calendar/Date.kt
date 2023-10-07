package com.example.onerowcalendar.one_row_calendar

import java.time.LocalDate
import java.time.temporal.ChronoField

class Date(localDate: LocalDate) {
    val dayOfWeek: Int
    val dayOfMonth: Int
    val month: Int
    val weekNum: Int

    var isSelected = false

    init {
        dayOfMonth = localDate.dayOfMonth
        dayOfWeek = localDate.dayOfWeek.value
        month = localDate.month.value
        weekNum = localDate[ChronoField.ALIGNED_WEEK_OF_YEAR]
    }

    override fun hashCode(): Int {
        return dayOfWeek * dayOfMonth * month * weekNum
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Date

        if (dayOfWeek != other.dayOfWeek) return false
        if (dayOfMonth != other.dayOfMonth) return false
        if (month != other.month) return false
        if (weekNum != other.weekNum) return false
        if (isSelected != other.isSelected) return false

        return true
    }
}