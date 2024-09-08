package com.example.accountbook.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbook.Constants.CategoryConstants
import com.example.accountbook.Data.TransactionsCategoryGroupData
import com.example.accountbook.Data.TransactionsData
import com.example.accountbook.R
import java.text.DecimalFormat

class TransactionsCategoryAdapter(
    private val transactions: List<TransactionsCategoryGroupData>
) : RecyclerView.Adapter<TransactionsCategoryAdapter.TransactionsViewHolder>() {

    class TransactionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImageView: ImageView = view.findViewById(R.id.rowCategoryImageView)
        val categoryNameTextView: TextView = view.findViewById(R.id.rowCategoryNameTextView)
        val contentTextView: TextView = view.findViewById(R.id.rowContentTextView)
        val amountTextView: TextView = view.findViewById(R.id.rowAmountTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_transaction, parent, false)
        return TransactionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val transaction = transactions[position]
        val isExpense = transaction.balanceType == 0
        val icon = if (isExpense) R.drawable.shopping_cart_24 else R.drawable.savings_24
        // 金額のカンマ区切り
        val decimalFormat = DecimalFormat("#,###")
        val formatAmount = decimalFormat.format(transaction.amount)
        // 合計
        val sumAmount = transactions.sumOf { it.amount }
        val percent = "${calculatePercentage(transaction.amount, sumAmount) ?: "--"}" + "%"
        Log.d("percent", "amount: ${transaction.amount}, sumAmount: ${sumAmount}, percent: ${transaction.amount / sumAmount * 100.0}")

        holder.categoryImageView.setImageResource(icon)
        holder.categoryNameTextView.text = if (isExpense) CategoryConstants.expensesCategories[transaction.categoryType] else CategoryConstants.incomeCategories[transaction.categoryType]
        holder.contentTextView.text = percent
        holder.amountTextView.text = if (isExpense) "-${formatAmount}円" else "+${formatAmount}円"
        Log.d("TransactionsCategoryAdapter", "Position: $position, Transaction: $transaction")
    }

    override fun getItemCount(): Int = transactions.size

    private fun calculatePercentage(amount: Int, total: Int): Double? {
        return if (total != 0) {
            val percentage = (amount.toDouble() / total) * 100.0
            Math.round(percentage * 10.0) / 10.0
        } else {
            null
        }
    }
}