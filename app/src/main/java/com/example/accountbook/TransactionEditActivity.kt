package com.example.accountbook

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.accountbook.common.DatePick

class TransactionEditActivity : ParentActivity(), DatePick.DatePickerListener {

    var transactionId: String = ""
    var transactionData: TransactionData = TransactionData()
    private var lastEventAction: Int? = null
    val categories = mapOf(
        1 to "食費",
        2 to "日用品",
        3 to "趣味",
        4 to "交際費",
        5 to "交通費",
        6 to "衣服・美容",
        7 to "健康・医療",
        8 to "自動車",
        9 to "教育",
        10 to "特別な支出",
        11 to "光熱費",
        12 to "通信費",
        13 to "住宅",
        14 to "税金",
        15 to "保険",
        16 to "その他"
    )
    private var selectedCategoryId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_edit)

        // カテゴリ選択ボタン
        val categorySpinner: Spinner = findViewById(R.id.categorySpinner)

        // Spinnerにカテゴリを設定
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.values.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

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

    override fun getActionBarConfig(): ActionBarConfig {
        var rightDisplayMode = ActionBarDisplayMode.NONE
        var rightText: String? = null
        if (!transactionId.isBlank()) {
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
            // 画面を戻す
            onBackPressedDispatcher.onBackPressed()
        }
    }

    // modelDataへの格納
    private fun setData() {
        var balanceType:Int? = null
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
                // 何も選択されていない場合の処理
                balanceType = null
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
        transactionData = TransactionData(
            transactionId = transactionId,
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
        // 収支の入力チェック
        if (transactionData.balanceType == null) {
            // 0か1以外というチェックでも良い
            val errorLabel = findViewById<TextView>(R.id.balanceErrorLabel)
            errorLabel.text = getString(R.string.validError_balance_null)
            isValid = false
        }

        // カテゴリの入力チェック
        if (transactionData.categoryType == null) {
            val errorLabel = findViewById<TextView>(R.id.categoryErrorLabel)
            errorLabel.text = getString(R.string.validError_balance_null)
            isValid = false
        }

        // 日付の入力チェック
        if (transactionData.date == null) {
            val errorLabel = findViewById<TextView>(R.id.dateErrorLabel)
            errorLabel.text = getString(R.string.validError_date_null)
            isValid = false
        }

        // 金額の入力チェック
        if (transactionData.amount == null) {
            val errorLabel = findViewById<TextView>(R.id.amountErrorLabel)
            errorLabel.text = getString(R.string.validError_amount_null)
            isValid = false
        }

        // 内容の入力チェック
        if (transactionData.content.isNullOrBlank()) {
            val errorLabel = findViewById<TextView>(R.id.contentErrorLabel)
            errorLabel.text = getString(R.string.validError_content_null)
            isValid = false
        }

        return isValid
    }
}

public data class TransactionData (
    var transactionId:String = "",
    var balanceType:Int? = null,
    var categoryType:Int? = null,
    var date:String? = null,
    var amount:Int? = null,
    var content:String? = null
)