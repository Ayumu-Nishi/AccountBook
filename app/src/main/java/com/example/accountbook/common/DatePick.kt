package com.example.accountbook.common

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.accountbook.RegisterActivity


class DatePick: DialogFragment(), DatePickerDialog.OnDateSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        // depreacatedなのでいつか修正したいが対案がない（公式が置き換え先に指定しているものはドラムロールはなさそう）
        val datePickerDialog = DatePickerDialog(requireContext(), AlertDialog.THEME_HOLO_LIGHT ,activity as RegisterActivity, year, month, day)
        datePickerDialog.datePicker.minDate = getBeforeYear(-120) // 120年前
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // 現在
        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        // Activity側でoverrideすること
    }

    // TODO: 共通クラスに移動しても良い
    // 現在から過去に指定の年数遡った時のカレンダーの値を取得
    private fun getBeforeYear(int: Int): Long {
        val calender = Calendar.getInstance()
        calender.add(Calendar.YEAR, int)
        return calender.timeInMillis
    }

}