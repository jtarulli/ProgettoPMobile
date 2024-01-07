package com.example.progettopm

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MyDatePicker(ctx: Context) : DatePicker(ctx) {

    private val context: Context = ctx
    private var timeLocal: LocalDateTime = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.of("GMT+1"))

    fun getTimeLocal(): LocalDateTime = timeLocal

    fun setOnDateChangedListener(listener: () -> Unit){
        this.init(0, 0, 0) { _, _, _, _ ->
            listener.invoke()
        }
    }

    private fun parseToDt(data : Instant) : LocalDateTime {
        return LocalDateTime.ofInstant(data, ZoneId.of("GMT+1"))
    }

    fun mostraSceltaDate(data: Instant) {
        val calendar = Calendar.getInstance()
        val yy = parseToDt(data).year
        val mm = parseToDt(data).monthValue
        val dd = parseToDt(data).dayOfMonth

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                timeLocal = LocalDateTime.of(year, month, dayOfMonth, 0,0)
                mostraSceltaOra(calendar)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.updateDate(yy, mm, dd)
        datePickerDialog.show()
    }

    private fun mostraSceltaOra(calendar: Calendar) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                timeLocal = LocalDateTime.of(timeLocal.year,
                                             timeLocal.monthValue,
                                             timeLocal.dayOfYear,
                                             hourOfDay,
                                             minute)
                this.updateDate(timeLocal.year, timeLocal.monthValue, timeLocal.dayOfYear)
                //val inizio = calendar.timeInMillis
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}