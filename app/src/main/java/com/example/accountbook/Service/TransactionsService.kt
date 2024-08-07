package com.example.accountbook.Service

import android.util.Log
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
            Log.d("get", "target: ${entities}")
            entities.map { fromEntityToData(it) } // EntitiesをDataに変換
        }
    }

    suspend fun deleteTransactions(realm: Realm, id: String) {
        withContext(Dispatchers.IO) {
            realm.write {
                val frozenEntity = realm.query<TransactionsEntity>("transactionId == $0", id).find().firstOrNull()
                if (frozenEntity != null) {
                    val liveEntity = findLatest(frozenEntity)
                    if (liveEntity != null) {
                        Log.d("delete", "target: ${liveEntity}")
                        delete(liveEntity)
                        val afterEntity = realm.query<TransactionsEntity>().find()
                        Log.d("delete", "after: ${afterEntity}")
                    } else {
                        Log.e("delete", "Live entity not found for frozen entity with ID: $id")
                    }
                } else {
                    Log.e("delete", "Entity with ID $id not found.")
                }
            }
        }
    }

    fun groupTransactionsByDate(transactions: List<TransactionsData>): List<TransactionsGroupData> {
        return transactions.groupBy { it.date }
            .toSortedMap(compareByDescending { it }) // 日付を降順にソート
            .map { TransactionsGroupData(it.key ?: "Unknown date", it.value) }
    }

}