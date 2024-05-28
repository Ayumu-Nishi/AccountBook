package com.example.accountbook

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.accountbook.common.DatePick

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private var lastEventAction: Int? = null
    private var mailAddressText: String = ""
    private var passwordText: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // アクションバーに戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.registerActivityTitle)

        // 生年月日選択ボタン
        val birthdaySelectButton = findViewById<Button>(R.id.birthdaySelectButton)
        birthdaySelectButton.setOnClickListener {
            val dateFragment = DatePick()
            dateFragment.show(supportFragmentManager, "datePicker")
        }

        // 登録ボタン
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        signUpButton.setOnClickListener {
            signUp()
        }

        // 利用規約のwebView
        val webView = findViewById<WebView>(R.id.agreementWebView)
        webView.loadUrl("https://developer.android.com/?hl=ja")
    }

    // 生年月日ピッカーで選択した日付を取得して、ラベルに表示する処理
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val birthdayDisplayLabel = findViewById<TextView>(R.id.birthdayDisplayLabel)
        val selectedDate = "$year/${month.plus(1)}/$dayOfMonth"
        birthdayDisplayLabel.text = selectedDate
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

    // 登録ボタン押下時の処理
    // サインイン処理
    private fun signUp() {
        // 入力チェック
        if (isValidate()) {
            // サインイン処理

        }
    }

    // 入力チェック
    private fun isValidate(): Boolean {
        var isValid = true
        // メールアドレスの入力チェック
        val mailAddressEditText = findViewById<EditText>(R.id.editTextEmailAddress)
        mailAddressText = mailAddressEditText.text.toString()
        val mailAddressErrorLabel = findViewById<TextView>(R.id.mailAdressErrorLabel)
        if (mailAddressText.isEmpty()) {
            // エラーメッセージの表示
            mailAddressErrorLabel.text = getString(R.string.validError_mailAddress_blank)
            // エラーあり
            isValid = false
        } else if (!mailAddressText.contains("@")) {
            // エラーメッセージの表示
            mailAddressErrorLabel.text = getString(R.string.validError_mailAddress_form)
            // エラーあり
            isValid = false
        } else {
            mailAddressErrorLabel.text = ""
        }

        // パスワードの入力チェック
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        passwordText = passwordEditText.text.toString()
        val passwordErrorLabel = findViewById<TextView>(R.id.passwordErrorLabel)
        if (passwordText.isEmpty()) {
            // エラーメッセージの表示
            passwordErrorLabel.text = getString(R.string.validError_password_blank)
            // エラーあり
            isValid = false
        } else if (passwordText.length < 8) {
            // エラーメッセージの表示
            passwordErrorLabel.text = getString(R.string.validError_password_length)
            // エラーあり
            isValid = false
        } else {
            passwordErrorLabel.text = ""
        }

        // 生年月日の入力チェック
        val birthdayDisplayLabel = findViewById<TextView>(R.id.birthdayDisplayLabel)
        val birthdayText = birthdayDisplayLabel.text.toString()
        val birthdayErrorLabel = findViewById<TextView>(R.id.birthdayErrorLabel)
        if (birthdayText.isEmpty() || birthdayText == getString(R.string.birthdayPlaceHolder)) {
            // エラーメッセージの表示
            birthdayErrorLabel.text = getString(R.string.validError_birthday_blank)
            // エラーあり
            isValid = false
        } else {
            birthdayErrorLabel.text = ""
        }

        // 利用規約の同意チェック
        val agreementCheckBox = findViewById<CheckBox>(R.id.agreementCheckBox)
        val agreementErrorLabel = findViewById<TextView>(R.id.agreementErrorLabel)
        if (!agreementCheckBox.isChecked) {
            // エラーメッセージの表示
            agreementErrorLabel.text = getString(R.string.validError_agreement_false)
            // エラーあり
            isValid = false
        } else {
            agreementErrorLabel.text = ""
        }
        return isValid
    }

    // 05_入出金明細画面への遷移処理
    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    // 戻るボタン押下時の処理
    override fun onSupportNavigateUp(): Boolean {
            onBackPressedDispatcher.onBackPressed()
        return true
    }
}