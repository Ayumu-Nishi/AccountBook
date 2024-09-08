package com.example.accountbook.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbook.Constants.CategoryConstants
import com.example.accountbook.Constants.CategoryConstants.expensesCategories
import com.example.accountbook.Constants.CategoryConstants.incomeCategories
import com.example.accountbook.Data.TransactionsData
import com.example.accountbook.R
import java.text.DecimalFormat

class TransactionsAdapter(
    private val transactions: List<TransactionsData>,
    private val onItemClick: (TransactionsData) -> Unit
) : RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

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
        val icon = if (isExpense) CategoryConstants.expensesIcons[transaction.categoryType] ?: R.drawable.shopping_cart_24 else CategoryConstants.incomeIcons[transaction.categoryType] ?: R.drawable.savings_24
        // 金額のカンマ区切り
        val decimalFormat = DecimalFormat("#,###")
        val formatAmount = decimalFormat.format(transaction.amount)

        holder.categoryImageView.setImageResource(icon)
        holder.categoryNameTextView.text = if (isExpense) expensesCategories[transaction.categoryType] else incomeCategories[transaction.categoryType]
        holder.contentTextView.text = transaction.content
        holder.amountTextView.text = if (isExpense) "-${formatAmount}円" else "+${formatAmount}円"
        Log.d("TransactionsAdapter", "Position: $position, Transaction: $transaction")

        holder.itemView.setOnClickListener {
            onItemClick(transaction) // クリックされたアイテムの transactionId を返す
        }
    }

    override fun getItemCount(): Int = transactions.size
}