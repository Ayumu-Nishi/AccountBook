package com.example.accountbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // アクションバーに戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.LoginActivityTitle)

        // ログインボタン
        val signUpButton = findViewById<Button>(R.id.signInButton)
        signUpButton.setOnClickListener {
            signIn()
        }
    }

    // サインイン処理
    fun signIn() {

    }

    // 戻るボタン押下時の処理
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}