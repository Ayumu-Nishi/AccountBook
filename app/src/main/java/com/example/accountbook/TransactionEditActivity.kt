package com.example.accountbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TransactionEditActivity : ParentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_edit)

    }

    override fun getActionBarConfig(): ActionBarConfig {
        return ActionBarConfig(
            title = getString(R.string.TransactionEditActivityTitle),
            leftDisplayMode = ActionBarDisplayMode.TEXT,
            leftText = getString(R.string.actionBar_leftButtonTitle),
            leftIconResId = null,
            rightDisplayMode = ActionBarDisplayMode.TEXT,
            rightText = getString(R.string.actionBar_rightButtonTitle),
            rightIconResId = null
        )
    }

    // 取消ボタン押下時
    override fun onLeftContainerClicked() {
        super.onLeftContainerClicked()
        onBackPressedDispatcher.onBackPressed()
    }

    // 保存ボタン押下時
    override fun onRightContainerClicked() {
        super.onRightContainerClicked()
        // 保存処理
    }
}