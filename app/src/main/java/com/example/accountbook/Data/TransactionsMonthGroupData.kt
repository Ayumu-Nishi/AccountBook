package com.example.accountbook.Data

public data class TransactionsMonthGroupData (
    val date: String,
    val transactions: List<TransactionsCategoryGroupData>
)