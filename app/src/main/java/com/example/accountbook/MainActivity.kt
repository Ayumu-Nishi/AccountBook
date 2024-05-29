package com.example.accountbook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // MyApplicationクラスのインスタンスを取得
        val myApp = applicationContext as MyApplication

        // MyApplicationで定義したauth変数を参照してログイン状態を確認
        val currentUser = myApp.auth.currentUser
        if (currentUser != null) {
            print("ログイン済み")
            // ログイン済みの場合はログイン後の画面に遷移
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            print("未ログイン")
            // 未ログインの場合はログイン前の画面に遷移
            startActivity(Intent(this, StartActivity::class.java))
        }
        // 画面の表示を行わないため、setContentView()メソッドを呼び出さない
        // finish()メソッドを呼び出して、このActivityを終了させる
        finish()
    }
}