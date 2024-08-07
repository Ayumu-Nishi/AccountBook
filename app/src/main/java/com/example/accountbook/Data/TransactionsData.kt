package com.example.accountbook.Data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
public data class TransactionsData (
    var transactionId:String = "",
    var balanceType:Int = 0,
    var categoryType:Int = 1,
    var date:String? = null,
    var amount:Int? = null,
    var content:String? = null
) : Parcelable