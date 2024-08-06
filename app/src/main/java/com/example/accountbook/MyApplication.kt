package com.example.accountbook

import com.example.accountbook.Entity.TransactionsEntity
import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApplication : Application() {
    internal lateinit var auth: FirebaseAuth
    lateinit var realm: Realm
    override fun onCreate() {
        super.onCreate()

        // アプリ起動時に行いたい処理をここに記述する
        // FirebaseAuthの初期化
        auth = Firebase.auth
        // realmの初期化
        setRealm()
    }

    private fun setRealm() {
        val config = RealmConfiguration.create(schema = setOf(TransactionsEntity::class))
        realm = Realm.open(config)
    }

}