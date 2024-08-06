package com.example.accountbook.Service

import com.example.accountbook.Data.TransactionsData
import com.example.accountbook.Data.TransactionsGroupData
import com.example.accountbook.Entity.TransactionsEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class TransactionsService {

    fun fromDataToEntity(transactionsData: TransactionsData): TransactionsEntity {
        return TransactionsEntity().apply {
            transactionId = if (transactionsData.transactionId.isEmpty()) UUID.randomUUID().toString() else transactionsData.transactionId
            balanceType = transactionsData.balanceType
            categoryType = transactionsData.categoryType
            date = transactionsData.date
            amount = transactionsData.amount
            content = transactionsData.content
        }
    }

    fun fromEntityToData(transactionsEntity: TransactionsEntity): TransactionsData {
        return TransactionsData(
            transactionId = transactionsEntity.transactionId,
            balanceType = transactionsEntity.balanceType,
            categoryType = transactionsEntity.categoryType,
            date = transactionsEntity.date,
            amount = transactionsEntity.amount,
            content = transactionsEntity.content
        )
    }

    suspend fun saveTransaction(realm: Realm, transactionData: TransactionsData) {
        withContext(Dispatchers.IO) { // I/Oディスパッチャーを使用
            realm.write {
                val entity = fromDataToEntity(transactionData)
                copyToRealm(entity, UpdatePolicy.ALL)
            }
        }
    }

    suspend fun getTransactions(realm: Realm): List<TransactionsData> {
        return withContext(Dispatchers.IO) {
            // 全件取得
            val entities = realm.query<TransactionsEntity>().find()
            entities.map { fromEntityToData(it) } // EntitiesをDataに変換
        }
    }

    fun groupTransactionsByDate(transactions: List<TransactionsData>): List<TransactionsGroupData> {
        return transactions.groupBy { it.date }
            .toSortedMap(compareByDescending { it }) // 日付を降順にソート
            .map { TransactionsGroupData(it.key ?: "Unknown date", it.value) }
    }

}