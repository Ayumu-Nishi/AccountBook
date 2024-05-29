package com.example.accountbook

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyApplication : Application() {
    internal lateinit var auth: FirebaseAuth
    override fun onCreate() {
        super.onCreate()

        // アプリ起動時に行いたい処理をここに記述する
        // FirebaseAuthの初期化
        auth = Firebase.auth
    }
}