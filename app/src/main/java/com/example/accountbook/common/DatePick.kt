package com.example.accountbook.common

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.accountbook.RegisterActivity


class DatePick(private val initialYear: Int, private val initialMonth: Int, private val initialDay: Int) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface DatePickerListener {
        fun onDateSet(year: Int, month: Int, day: Int)
    }

    private lateinit var listener: DatePickerListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datePickerDialog = DatePickerDialog(
            requireContext(), AlertDialog.THEME_HOLO_LIGHT, this, initialYear, initialMonth, initialDay
        )
        datePickerDialog.datePicker.minDate = getBeforeYear(-120) // 120年前
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // 現在
        return datePickerDialog
    }

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        try {
            // 親アクティビティがリスナーを実装していることを確認
            listener = context as DatePickerListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DatePickerListener")
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        listener.onDateSet(year, month, day)
    }

    // TODO: 共通クラスに移動しても良い
    // 現在から過去に指定の年数遡った時のカレンダーの値を取得
    private fun getBeforeYear(int: Int): Long {
        val calender = Calendar.getInstance()
        calender.add(Calendar.YEAR, int)
        return calender.timeInMillis
    }
}