package com.jdccmobile.nasapi.ui.utils

import java.time.DayOfWeek
import java.time.LocalDate

fun LocalDate.getFirstDayOfWeek(): LocalDate = with(DayOfWeek.MONDAY)

fun LocalDate.getLastDayOfWeek(): LocalDate = with(DayOfWeek.SUNDAY)
