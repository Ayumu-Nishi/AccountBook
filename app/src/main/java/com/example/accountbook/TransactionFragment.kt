package com.example.accountbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.accountbook.Adapter.TransactionsAdapter
import com.example.accountbook.Adapter.TransactionsGroupAdapter
import com.example.accountbook.Data.TransactionsData
import com.example.accountbook.Data.TransactionsGroupData
import com.example.accountbook.Entity.TransactionsEntity
import com.example.accountbook.Service.TransactionsService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.launch
import java.util.logging.Logger

class TransactionFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionsGroupAdapter
    val config = RealmConfiguration.Builder(schema = setOf(TransactionsEntity::class)).build()
    val realm = Realm.open(config)
    var transactionsDatas: List<TransactionsData> = listOf()
    var transactionsGroupDatas: List<TransactionsGroupData> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setData()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        fab = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)

        if (::fab.isInitialized) {
            fab.setOnClickListener {
                val intent = Intent(requireActivity(), TransactionEditActivity::class.java)
                startActivity(intent)
            }
        }
        return view
    }

    private fun setData() {
        lifecycleScope.launch {
            transactionsDatas = TransactionsService().getTransactions(realm)
            transactionsGroupDatas = TransactionsService().groupTransactionsByDate(transactionsDatas)
            adapter = TransactionsGroupAdapter(transactionsGroupDatas)
            recyclerView.adapter = adapter
            Log.d("data", "${transactionsDatas.size}")
        }
    }
}
