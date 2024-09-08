package com.example.accountbook.Service

import android.util.Log
import com.example.accountbook.Data.TransactionsData
import com.example.accountbook.Data.TransactionsGroupData
import com.example.accountbook.Data.TransactionsMonthGroupData
import com.example.accountbook.Data.TransactionsCategoryGroupData
import com.example.accountbook.Entity.TransactionsEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    fun groupTransactionsByMonthAndCategory(transactions: List<TransactionsData>): List<TransactionsMonthGroupData> {
        // 1. 日付を "yyyy/MM" でグループ化
        val groupedByMonth = transactions.groupBy {
            changeDateStringFromSlashToKanji(it.date)
        }

        // 2. 各月ごとに balanceType と categoryType の組み合わせで再度グループ化し、amount を集計
        return groupedByMonth.map { (month, transactionsInMonth) ->
            val groupedByCategory = transactionsInMonth
                .filter { it.amount != null } // amount が null でないものを対象
                .groupBy { Pair(it.balanceType, it.categoryType) } // balanceType と categoryType のペアでグループ化
                .map { (key, transactionsInCategory) ->
                    val totalAmount = transactionsInCategory.sumOf { it.amount ?: 0 } // amount の合計を計算
                    TransactionsCategoryGroupData(
                        balanceType = key.first,
                        categoryType = key.second,
                        amount = totalAmount
                    )
                }

            // 3. 月ごとの TransactionsMonthGroupData を作成
            TransactionsMonthGroupData(
                date = month,
                transactions = groupedByCategory
            )
        }.sortedByDescending { it.date }
    }

    fun changeDateStringFromSlashToKanji(dateStr: String?): String {
        // スラッシュで分かれた日付文字列を解析するフォーマット
        val inputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy年MM月", Locale.getDefault())

        // 文字列が null でないことを確認
        if (dateStr.isNullOrEmpty()) return ""

        return try {
            // 日付文字列を解析
            val date = inputFormat.parse(dateStr)
            // 解析した日付を "yyyy年MM月" 形式に変換
            outputFormat.format(date)
        } catch (e: Exception) {
            // 解析に失敗した場合は空文字を返す
            ""
        }
    }


}