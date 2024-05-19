package com.example.accountbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // button
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            goToRegisterActivity()
        }
        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            goToLoginActivity()
        }
    }

    // 02_登録画面への遷移処理
    private fun goToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    //04_ログイン画面への遷移処理
    private fun goToLoginActivity() {

    }
}