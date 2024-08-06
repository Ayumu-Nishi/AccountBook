package com.example.accountbook.Entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class TransactionsEntity : RealmObject {
    @PrimaryKey
    var transactionId:String = ""
    var balanceType:Int = 0
    var categoryType:Int? = null
    var date:String? = null
    var amount:Int? = null
    var content:String? = null
}