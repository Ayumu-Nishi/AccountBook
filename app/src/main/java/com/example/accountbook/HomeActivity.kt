package com.example.accountbook

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = getString(R.string.homeActivityTitle)

        // addCallbackでコールバックを追加
        onBackPressedDispatcher.addCallback(callback)
    }

    //OnBackPressedCallbackのコンストラクタはtrueにすることでコールバックを有効にする
    private val callback = object : OnBackPressedCallback(true) {
        //コールバックのhandleOnBackPressedを呼び出して、戻るキーを押したときの処理を記述
        override fun handleOnBackPressed() {
            // 戻るキーを無効にする＝処理を書かない
            return
        }
    }

    // ログアウトボタンを追加するためのメニューの設定
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    // ログアウトボタン（メニューに配置したボタン）押下時の処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_button -> {
                // ボタンがクリックされた時の処理を記述
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}