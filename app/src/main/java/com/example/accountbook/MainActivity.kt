package com.example.accountbook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 画面の表示を行わないため、setContentView()メソッドを呼び出さない
        // finish()メソッドを呼び出して、このActivityを終了させる
        finish()
    }
}