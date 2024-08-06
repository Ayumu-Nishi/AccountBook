package com.example.accountbook

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.accountbook.Constants.CategoryConstants.expensesCategories
import com.example.accountbook.Constants.CategoryConstants.incomeCategories
import com.example.accountbook.Data.TransactionsData
import com.example.accountbook.Entity.TransactionsEntity
import com.example.accountbook.common.DatePick
import com.example.accountbook.Model.ActionBarConfig
import com.example.accountbook.Model.ActionBarDisplayMode
import com.example.accountbook.Service.TransactionsService
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.launch

class TransactionEditActivity : ParentActivity(), DatePick.DatePickerListener {

    // 前画面からの受け渡し＆画面内の入力データ保持用のクラス
    var transactionsData: TransactionsData = TransactionsData()
    // Realmインスタンス
    val config = RealmConfiguration.Builder(schema = setOf(TransactionsEntity::class)).build()
    val realm = Realm.open(config)
    // タッチイベントの検知用
    private var lastEventAction: Int? = null


    var categories = expensesCategories

    private var selectedCategoryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_edit)

        // 収支ラジオボタン
        val radioGroup = findViewById<RadioGroup>(R.id.balanceRadioGroup)
        val default = if (transactionsData.balanceType == 1) {
            findViewById<RadioButton>(R.id.incomeRadioButton)
        } else {
            findViewById<RadioButton>(R.id.expenseRadioButton)
        }
        radioGroup.check(default.id)

        // カテゴリ選択ボタン
        val categorySpinner: Spinner = findViewById(R.id.categorySpinner)

        // ラジオボタンの選択変更リスナーを設定
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.incomeRadioButton -> {
                    // "収入" が選択されている場合の処理
                    categories = incomeCategories
                }
                else -> {
                    // "支出" が選択されている場合の処理
                    categories = expensesCategories
                }
            }
            setCategory(categorySpinner)
        }

        // Spinnerにカテゴリを設定
        setCategory(categorySpinner)

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 選択されたカテゴリのIDを取得
                selectedCategoryId = categories.keys.elementAt(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 選択がクリアされた場合の処理
                selectedCategoryId = null
            }
        }

        // 日付選択ボタン
        val dateSelectButton = findViewById<Button>(R.id.dateSelectButton)
        dateSelectButton.setOnClickListener {
            val dateFragment = DatePick()
            dateFragment.show(supportFragmentManager, "datePicker")
        }

        // 登録ボタン
        val registerButton = findViewById<Button>(R.id.transactionRegisterButton)
        registerButton.setOnClickListener {
            register()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun getActionBarConfig(): ActionBarConfig {
        var rightDisplayMode = ActionBarDisplayMode.NONE
        var rightText: String? = null
        if (!transactionsData.transactionId.isBlank()) {
            // 編集の場合：削除ボタンを表示
            rightDisplayMode = ActionBarDisplayMode.TEXT
            rightText = getString(R.string.actionBar_rightButtonTitle)
        }

        return ActionBarConfig(
            title = getString(R.string.TransactionEditActivityTitle),
            leftDisplayMode = ActionBarDisplayMode.TEXT,
            leftText = getString(R.string.actionBar_leftButtonTitle),
            leftIconResId = null,
            rightDisplayMode = rightDisplayMode,
            rightText = rightText,
            rightIconResId = null
        )
    }

    // Spinnerにカテゴリを設定
    private fun setCategory(spinner: Spinner) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.values.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    // 生年月日ピッカーで選択した日付を取得して、ラベルに表示する処理
    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val dateDisplayLabel = findViewById<TextView>(R.id.dateDisplayTextView)
        val selectedDate = "$year/${month.plus(1)}/$dayOfMonth"
        dateDisplayLabel.text = selectedDate
    }

    // タッチイベント
    // スクロール以外のタッチイベントでキーボードを閉じるように設定
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        // スクロール時のタップを無視させる
        if (lastEventAction != MotionEvent.ACTION_MOVE && event?.action == MotionEvent.ACTION_UP) {
            val amountEditText = findViewById<EditText>(R.id.amountEditText)
            val contentEditText = findViewById<EditText>(R.id.contentEditText)
            if (!isEditTextTouch(event.rawX.toInt(), event.rawY.toInt(), amountEditText, contentEditText)) {
                // タップ位置がEditText以外の場合
                // キーボードを非表示にして、フォーカスを外す
                val constraintLayout = findViewById<ConstraintLayout>(R.id.registerTransactionConstraintLayout)
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

    // 戻るボタン押下時
    override fun onLeftContainerClicked() {
        super.onLeftContainerClicked()
        onBackPressedDispatcher.onBackPressed()
    }

    // 削除ボタン押下時
    override fun onRightContainerClicked() {
        super.onRightContainerClicked()
        // 削除処理
    }

    // 登録ボタン押下時
    private fun register() {
        setData()
        if (isValidate()) {
            // Realmへの保存処理
            saveData()
        }
    }

    // modelDataへの格納
    private fun setData() {
        var balanceType:Int = 0
        var categoryType:Int? = null
        var date:String? = null
        var amount:Int? = null
        var content:String? = null

        // 収支
        val radioGroup: RadioGroup = findViewById(R.id.balanceRadioGroup)
        // 選択されている RadioButton の ID に基づいて処理を行う
        when (radioGroup.checkedRadioButtonId) {
            R.id.expenseRadioButton -> {
                // "Expense" が選択されている場合の処理
                balanceType = 0
            }
            R.id.incomeRadioButton -> {
                // "Income" が選択されている場合の処理
                balanceType = 1
            }
            else -> {
                // 未選択もしくはそれ以外が選択されている場合
                balanceType = 0
            }
        }

        // カテゴリ
        categoryType = selectedCategoryId
        // 日付
        val dateDisplayLabel = findViewById<TextView>(R.id.dateDisplayTextView)
        val dateStr = dateDisplayLabel.text.toString()
        if (dateStr == getString(R.string.dateDisplayLabel)) {
            date = null
        } else {
            date = dateStr
        }
        // 金額
        val amountEditText = findViewById<EditText>(R.id.amountEditText)
        amount = amountEditText.text.toString().toIntOrNull()
        // 内容
        val contentEditText = findViewById<EditText>(R.id.contentEditText)
        content = contentEditText.text.toString()
        // transactionIdを除いてDataクラスに格納
        transactionsData = transactionsData.copy(
            balanceType = balanceType,
            categoryType = categoryType,
            date = date,
            amount = amount,
            content = content
        )

    }

    // 入力チェック
    private fun isValidate(): Boolean {
        var isValid = true

        // カテゴリの入力チェック
        if (transactionsData.categoryType == null) {
            val errorLabel = findViewById<TextView>(R.id.categoryErrorLabel)
            errorLabel.text = getString(R.string.validError_balance_null)
            isValid = false
        }

        // 日付の入力チェック
        if (transactionsData.date == null) {
            val errorLabel = findViewById<TextView>(R.id.dateErrorLabel)
            errorLabel.text = getString(R.string.validError_date_null)
            isValid = false
        }

        // 金額の入力チェック
        if (transactionsData.amount == null) {
            val errorLabel = findViewById<TextView>(R.id.amountErrorLabel)
            errorLabel.text = getString(R.string.validError_amount_null)
            isValid = false
        }

        // 内容の入力チェック
        if (transactionsData.content.isNullOrBlank()) {
            val errorLabel = findViewById<TextView>(R.id.contentErrorLabel)
            errorLabel.text = getString(R.string.validError_content_null)
            isValid = false
        }

        return isValid
    }

    private fun saveData() {
        lifecycleScope.launch {
            TransactionsService().saveTransaction(realm, transactionsData)
            AlertDialog.Builder(this@TransactionEditActivity)
                .setTitle("登録完了")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss() // ダイアログを閉じる
                    // 画面を戻す
                    onBackPressedDispatcher.onBackPressed()
                }
                .show()
        }
    }
}