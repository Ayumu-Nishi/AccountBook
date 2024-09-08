package com.example.accountbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbook.Adapter.TransactionsAdapter
import com.example.accountbook.Adapter.TransactionsCategoryAdapter
import com.example.accountbook.Adapter.TransactionsGroupAdapter
import com.example.accountbook.Data.TransactionsData
import com.example.accountbook.Data.TransactionsCategoryGroupData
import com.example.accountbook.Data.TransactionsMonthGroupData
import com.example.accountbook.Entity.TransactionsEntity
import com.example.accountbook.Service.TransactionsService
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BalanceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionsCategoryAdapter
    private lateinit var rightButton: Button
    private lateinit var incomeTextView: TextView
    private lateinit var spendingTextView: TextView
    private lateinit var balanceTextView: TextView
    private var ymTextView: TextView? = null
    private val config = RealmConfiguration.Builder(schema = setOf(TransactionsEntity::class)).build()
    private val realm = Realm.open(config)
    private var transactionsDatas: List<TransactionsData> = listOf()
    private var transactionsCategoryDatas:List<TransactionsCategoryGroupData>  = listOf()
    private var transactionsCategoryIncomeDatas: List<TransactionsCategoryGroupData> = listOf()
    private var transactionsCategorySpendingDatas: List<TransactionsCategoryGroupData> = listOf()
    private var transactionsMonthDatas: List<TransactionsMonthGroupData> = listOf()
    private var currentMonth = SimpleDateFormat("yyyy年MM月", Locale.getDefault()).format(Date())
    private lateinit var showMonth: String
    private var balanceType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showMonth = currentMonth
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_balance, container, false)
        // recyclerViewの設定
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        // 年月表示テキストの取得
        ymTextView =  view.findViewById(R.id.ymTextView)
        // 前月ボタンの設定
        val leftButton = view.findViewById<Button>(R.id.leftButton)
        leftButton.setOnClickListener {
            getDate(showMonth, -1)?.let {
                showMonth = it
            }
            setData()
        }
        // 翌月ボタンの設定
        rightButton = view.findViewById(R.id.rightButton)
        rightButton.setOnClickListener {
            if (compareDates(showMonth, currentMonth) == -1) {
                getDate(showMonth, +1)?.let {
                    showMonth = it
                }
                setData()
            }
        }
        // 収支表示領域の取得
        incomeTextView = view.findViewById(R.id.incomeValueTextView)
        spendingTextView = view.findViewById(R.id.spendingValueTextView)
        balanceTextView = view.findViewById(R.id.balanceValueTextView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val leftToggleButton = view.findViewById<Button>(R.id.leftToggleButton)
        val rightToggleButton = view.findViewById<Button>(R.id.rightToggleButton)

        // デフォルトで左ボタンを選択状態にする
        if (balanceType == 0) {
            updateButtonState(leftToggleButton, true)
            updateButtonState(rightToggleButton, false)
        } else {
            updateButtonState(leftToggleButton, false)
            updateButtonState(rightToggleButton, true)
        }
        leftToggleButton.setOnClickListener {
            if (!it.isSelected) {
                // 収入->支出
                updateButtonState(leftToggleButton, true)
                updateButtonState(rightToggleButton, false)
                balanceType = 0
                setData()
            }
        }

        rightToggleButton.setOnClickListener {
            if (!it.isSelected) {
                // 支出->収入
                updateButtonState(leftToggleButton, false)
                updateButtonState(rightToggleButton, true)
                balanceType = 1
                setData()
            }
        }
    }

    private fun setData() {
        lifecycleScope.launch {
            transactionsDatas = TransactionsService().getTransactions(realm)
            transactionsMonthDatas = TransactionsService().groupTransactionsByMonthAndCategory(transactionsDatas)
            Log.d("transactionsMonthDatas", "${transactionsMonthDatas}")
            val currentMonthIndex = transactionsMonthDatas.indexOfFirst { it.date == showMonth }
            Log.d("currentMonthIndex", "${currentMonthIndex}")
            var showDatas: List<TransactionsCategoryGroupData> = listOf()
            if (currentMonthIndex >= 0) {
                transactionsCategoryDatas = transactionsMonthDatas[currentMonthIndex].transactions
                transactionsCategorySpendingDatas =
                    transactionsCategoryDatas.filter { it.balanceType == 0 }
                transactionsCategoryIncomeDatas =
                    transactionsCategoryDatas.filter { it.balanceType == 1 }
                if (balanceType == 0) {
                    showDatas = transactionsCategorySpendingDatas
                } else if (balanceType == 1) {
                    showDatas = transactionsCategoryIncomeDatas
                }
            }
            adapter = TransactionsCategoryAdapter(showDatas)
            recyclerView.adapter = adapter
            Log.d("data", "${transactionsDatas.size}")

            ymTextView?.text = showMonth
            updateRightButton()
            updateBalanceText()
        }
    }

    private fun updateButtonState(button: Button, isSelected: Boolean) {
        if (isSelected) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainColor))
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.mainColor))
        }
        button.isSelected = isSelected

        // 背景の確認
        val backgroundDrawable = button.background
        Log.d("ButtonBackground", "Button ID: ${button.id}, Background: ${backgroundDrawable}")
    }

    private fun updateRightButton() {
        compareDates(showMonth, currentMonth)?.let {
            if (it == -1) {
                rightButton.isEnabled = true
                rightButton.setTextColor(getResources().getColor(R.color.mainColor))
            } else if (it >= 0) {
                rightButton.isEnabled = false
                rightButton.setTextColor(getResources().getColor(R.color.lightGray))
            }
        }
    }

    private fun updateBalanceText() {
        val income = transactionsCategoryIncomeDatas.sumOf { it.amount }
        incomeTextView.text = splitByComma(income)
        val spending = transactionsCategorySpendingDatas.sumOf { it.amount }
        spendingTextView.text = splitByComma(spending)
        balanceTextView.text = splitByComma(income - spending)
        if (income - spending > 0) {
            balanceTextView.setTextColor(getResources().getColor(R.color.blue))
        } else if (income - spending < 0) {
            balanceTextView.setTextColor(getResources().getColor(R.color.errorRed))
        } else {
            balanceTextView.setTextColor(getResources().getColor(R.color.black))
        }

    }

    private fun getDate(dateStr: String, month: Int): String? {
        val inputFormat = SimpleDateFormat("yyyy年MM月", Locale.getDefault())
        return try {
            val date = inputFormat.parse(dateStr)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MONTH, month)
            val outputFormat = SimpleDateFormat("yyyy年MM月", Locale.getDefault())
            outputFormat.format(calendar.time)
        } catch (e: ParseException) {
            // パースエラーの場合は null を返す
            null
        }
    }

    // 対象が比較先よりも前であれば-1、一致していれば0、後であれば1を返却
    // target: 対象
    // compare: 比較先
    private fun compareDates(target: String, compare: String): Int? {
        val format = SimpleDateFormat("yyyy年M月", Locale.getDefault())
        return try {
            val date1 = format.parse(target)
            val date2 = format.parse(compare)
            when {
                date1 == null || date2 == null -> null
                date1.before(date2) -> -1
                date1.after(date2) -> 1
                else -> 0
            }
        } catch (e: ParseException) {
            // パースエラーの場合は null を返す
            null
        }
    }

    private fun splitByComma(int: Int): String {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(int)
    }
}