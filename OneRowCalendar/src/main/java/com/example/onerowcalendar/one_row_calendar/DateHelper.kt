package com.example.onerowcalendar.one_row_calendar

import java.time.LocalDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

object DateHelper {

    private val weekdays = arrayOf(
        arrayOf("понедельник", "пнд", "пн"),
        arrayOf("вторник", "втр", "вт"),
        arrayOf("среда", "срд", "ср"),
        arrayOf("четверг", "чтв", "чт"),
        arrayOf("пятница", "птн", "пт"),
        arrayOf("суббота", "сбт", "сб"),
        arrayOf("воскресенье", "вск", "вс")
    )

    private val months = arrayOf(
        arrayOf("январь", "января", "янв"),
        arrayOf("февраль", "февраля", "фев"),
        arrayOf("март", "марта", "мар"),
        arrayOf("апрель", "апреля", "апр"),
        arrayOf("май", "мая", "мая"),
        arrayOf("июнь", "июня", "июня"),
        arrayOf("июль", "июля", "июля"),
        arrayOf("август", "августа", "авг"),
        arrayOf("сентябрь", "сентября", "сент"),
        arrayOf("октябрь", "октября", "окт"),
        arrayOf("ноябрь", "ноября", "нояб"),
        arrayOf("декабрь", "декабря", "дек")
    )

    //TODO Придумать как обновлять это свойство (смещение 1-ой учебной недели, относительно начала года)
    private var offsetStartWeek = 34

    fun getWeekdayNum(weekday: String): Int {
        return when (weekday.lowercase()) {
            weekdays[0][1] -> 1
            weekdays[1][1] -> 2
            weekdays[2][1] -> 3
            weekdays[3][1] -> 4
            weekdays[4][1] -> 5
            weekdays[5][1] -> 6
            weekdays[6][1] -> 7
            else -> -1
        }
    }

    fun getMonthNum(month: String): Int {
        return when (month.lowercase()) {
            months[0][1] -> 1
            months[1][1] -> 2
            months[2][1] -> 3
            months[3][1] -> 4
            months[4][1] -> 5
            months[5][1] -> 6
            months[6][1] -> 7
            months[7][1] -> 8
            months[8][1] -> 9
            months[9][1] -> 10
            months[10][1] -> 11
            months[11][1] -> 12
            else -> -1
        }
    }

    enum class MonthDisplayMode(value: Int) {
        GENITIVE_CASE(1),
        NOMINATIVE_CASE(0),
        THREE_CHAR(2)
    }

    fun getMonthName(month: Int, mode: MonthDisplayMode): String {
        return months[month - 1][mode.ordinal]
    }

    enum class WeekdayDisplayMode(value: Int) {
        FULL(0),
        THREE_CHAR(1),
        TWO_CHAR(2)
    }

    fun getWeekDayName(weekday: Int, mode: WeekdayDisplayMode): String {
        return weekdays[weekday - 1][mode.ordinal]
    }

    fun getCurrWeek(): Int {
        return LocalDate.now()[ChronoField.ALIGNED_WEEK_OF_YEAR] - offsetStartWeek
    }

    fun getCurrDayOfWeek(): Int {
        val dayOfWeek = LocalDate.now().dayOfWeek.value
        return if (dayOfWeek == 7) {
            1
        } else {
            dayOfWeek
        }
    }

    fun getDates(countWeeks: Int): MutableList<Date> {
        val numWeekStart = 1 + offsetStartWeek
        val numWeekEnd = countWeeks + offsetStartWeek

        val localDate1 = LocalDate.now()
            .with(ChronoField.DAY_OF_WEEK, 1)
            .with(ChronoField.ALIGNED_WEEK_OF_YEAR, numWeekStart.toLong())
        val localDate2 = LocalDate.now()
            .with(ChronoField.DAY_OF_WEEK, 7)
            .with(ChronoField.ALIGNED_WEEK_OF_YEAR, (numWeekEnd + 1).toLong())

        return getDatesBetween(localDate1, localDate2)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): MutableList<Date> {
        var numOfDaysBetween: Long = 0
        numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate)

        return (0 until numOfDaysBetween)
            .asSequence()
            .map { Date(startDate.plusDays(it)) }
            .filter { it.dayOfWeek != 7 }
            .toMutableList()
    }

    fun dateToString(date: Date): String {
        return "${getWeekDayName(date.dayOfWeek, WeekdayDisplayMode.FULL).replaceFirstChar { it.uppercase() }}, " +
                "${date.dayOfMonth} ${getMonthName(date.month, MonthDisplayMode.GENITIVE_CASE)}"
    }
}