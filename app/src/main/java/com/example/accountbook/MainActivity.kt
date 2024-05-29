package com.example.accountbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FirebaseDynamicLinksのインスタンスを取得
        val dynamicLinks = Firebase.dynamicLinks

        // Dynamic Linksを処理するリスナーを設定
        dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                // Dynamic Linkが存在する場合
//                val deepLink: Uri? = pendingDynamicLinkData?.link
//                if (deepLink != null) {
                    // リンクのパラメーターを取得し、適切な処理を行う
                    judgeNextActivity()
//                }
            }
            .addOnFailureListener(this) { e ->
                // Dynamic Linkの取得に失敗した場合の処理
                judgeNextActivity()
            }


        // 画面の表示を行わないため、setContentView()メソッドを呼び出さない
    }

    private fun judgeNextActivity() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            print("ログイン済み")
            // ログイン済みの場合はログイン後の画面に遷移
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            print("未ログイン")
            // 未ログインの場合はログイン前の画面に遷移
            startActivity(Intent(this, StartActivity::class.java))
        }
        finish()
    }
}