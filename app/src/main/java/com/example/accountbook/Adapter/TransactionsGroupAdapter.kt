package com.example.accountbook.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbook.Data.TransactionsGroupData
import com.example.accountbook.R

class TransactionsGroupAdapter(private val transactionGroups: List<TransactionsGroupData>) : RecyclerView.Adapter<TransactionsGroupAdapter.DateGroupViewHolder>() {

    class DateGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.rowTransactionDateTextView)
        val transactionRecyclerView: RecyclerView = view.findViewById(R.id.transactionRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_transaction_group, parent, false)
        return DateGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateGroupViewHolder, position: Int) {
        val transactionGroup = transactionGroups[position]
        holder.dateTextView.text = transactionGroup.date

        holder.transactionRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.transactionRecyclerView.adapter = TransactionsAdapter(transactionGroup.transactions)
        Log.d("TransactionsGroupAdapter", "Position: $position, Transaction: $transactionGroup")
    }

    override fun getItemCount(): Int = transactionGroups.size
}