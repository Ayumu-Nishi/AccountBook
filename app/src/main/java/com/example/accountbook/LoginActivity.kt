package com.example.accountbook

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private var lastEventAction: Int? = null
    private val auth = Firebase.auth
    private var mailAddressText: String = ""
    private var passwordText: String = ""
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

    // タッチイベント
    // スクロール以外のタッチイベントでキーボードを閉じるように設定
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // スクロール時のタップを無視させる
        if (lastEventAction != MotionEvent.ACTION_MOVE && event?.action == MotionEvent.ACTION_UP) {
            val mailAddressEditText = findViewById<EditText>(R.id.editTextEmailAddress)
            val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
            if (!isEditTextTouch(event.rawX.toInt(), event.rawY.toInt(), mailAddressEditText, passwordEditText)) {
                // タップ位置がEditText以外の場合
                // キーボードを非表示にして、フォーカスを外す
                val constraintLayout = findViewById<ConstraintLayout>(R.id.registerConstraintLayout)
                hideKeyboard(constraintLayout)
            }
        }
        lastEventAction = event?.action
        return super.dispatchTouchEvent(event)
    }

    // EditTextが選択状態かどうかを判定
    private fun isEditTextTouch(touchRawX: Int, touchRawY: Int, vararg editText: EditText): Boolean {
        return editText.any {
            val areaOutsideFocusedView = Rect()
            it.getGlobalVisibleRect(areaOutsideFocusedView)
            // 対象のEditTextの表示領域内のタップであるか判定
            return@any areaOutsideFocusedView.contains(touchRawX, touchRawY)
        }
    }

    // キーボード非表示処理
    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        view.clearFocus()
    }

    // サインイン処理
    private fun signIn() {
        val mailAddressEditText = findViewById<EditText>(R.id.editTextEmailAddress)
        mailAddressText = mailAddressEditText.text.toString()
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        passwordText = passwordEditText.text.toString()
        auth.signInWithEmailAndPassword(mailAddressText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    goToAuthcodeActivity()
                } else {
                    // セキュリティ上Exceptionの情報は隠蔽する
                    Toast.makeText(
                        baseContext,
                        "invalid EmailAddress or Password",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    // 03_認証メール送信画面への遷移処理
    private fun goToAuthcodeActivity() {
        val intent = Intent(this, AuthcodeActivity::class.java)
        startActivity(intent)
    }

    // 戻るボタン押下時の処理
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}