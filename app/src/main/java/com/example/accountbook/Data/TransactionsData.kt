package com.example.accountbook.Data

public data class TransactionsData (
    var transactionId:String = "",
    var balanceType:Int = 0,
    var categoryType:Int? = null,
    var date:String? = null,
    var amount:Int? = null,
    var content:String? = null
)