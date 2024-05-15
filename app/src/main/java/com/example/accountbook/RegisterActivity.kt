package com.example.accountbook

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView

class RegisterActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val birthdayDisplayLabel = findViewById<TextView>(R.id.birthdayDisplayLabel)
        val selectedDate = "$year/${month.plus(1)}/$dayOfMonth"
        birthdayDisplayLabel.text = selectedDate
    }

    private fun signUp() {
        // 入力チェック
        if (isValidate()) {
            // ログイン処理
            goToAuthcodeActivity()
        }
    }

    // 入力チェック
    private fun isValidate(): Boolean {
        var isValid = true
        // メールアドレスの入力チェック
        val mailAddressEditText = findViewById<EditText>(R.id.editTextEmailAddress)
        val mailAddressText = mailAddressEditText.text.toString()
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
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val passwordText = passwordEditText.text.toString()
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

    // 03_認証コード入力画面への遷移処理
    private fun goToAuthcodeActivity() {
        val intent = Intent(this, AuthcodeActivity::class.java)
        startActivity(intent)
    }
}