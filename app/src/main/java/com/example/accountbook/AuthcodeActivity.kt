package com.example.accountbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthcodeActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authcode)

        // アクションバーに戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.authcodeActivityTitle)

        auth.currentUser?.let {
            if (it.isEmailVerified) {
                // メール認証済みの場合
                goToHomeActivity()
            } else {
                // メール認証未済の場合
                sendEmailVerification(it)
            }
        }
    }

    // 05_入出金明細画面への遷移処理
    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    // 確認メールの送信
    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()?.addOnCompleteListener { task ->
            val textView = findViewById<TextView>(R.id.afterSendingTextView)
            val button = findViewById<Button>(R.id.authcodeButton)
            if (task.isSuccessful) {
                textView.text = getString(R.string.afterSendingText)
                button.text = getString(R.string.afterSendingButtonTitle)
                button.setOnClickListener {
                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                }
            } else {
                textView.text = getString(R.string.failureSendingText)
                button.text = getString(R.string.resendButtonTitle)
                button.setOnClickListener {
                    sendEmailVerification(user)
                }
            }
        }
    }

    // 戻るボタン押下時の処理
    override fun onSupportNavigateUp(): Boolean {
        // 確認メールで認証は終わっていないが、戻れるように、サインアウトしておく
        // 認証が完了した際はログイン画面からログインできる想定
        singOut()
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun singOut() {
        val myApp = applicationContext as MyApplication
        myApp.auth.signOut()
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}