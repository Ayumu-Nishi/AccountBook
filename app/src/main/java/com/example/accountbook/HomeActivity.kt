package com.example.accountbook

import com.example.accountbook.Model.ActionBarConfig
import com.example.accountbook.Model.ActionBarDisplayMode
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navController = findNavController(R.id.nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottomNavigation).setupWithNavController(
            navController
        )

        // addCallbackでコールバックを追加
        onBackPressedDispatcher.addCallback(callback)
    }

    override fun getActionBarConfig(): ActionBarConfig {
        return ActionBarConfig(
            title = getString(R.string.homeActivityTitle),
            leftDisplayMode = ActionBarDisplayMode.NONE,
            leftIconResId = null,
            leftText = null,
            rightDisplayMode = ActionBarDisplayMode.ICON,
            rightIconResId = R.drawable.logout,
            rightText = null
        )
    }

    // ログアウトボタン押下時
    override fun onRightContainerClicked() {
        super.onRightContainerClicked()
        AlertDialog.Builder(this) // FragmentではActivityを取得して生成
            .setTitle("ログアウトしますか？")
            .setMessage("")
            .setPositiveButton("OK") { dialog, which ->
                // ログアウトする
                singOut()
            }
            .setNegativeButton("キャンセル") { dialog, which ->
                // 何もしない
            }
            .show()
    }

    //OnBackPressedCallbackのコンストラクタはtrueにすることでコールバックを有効にする
    private val callback = object : OnBackPressedCallback(true) {
        //コールバックのhandleOnBackPressedを呼び出して、戻るキーを押したときの処理を記述
        override fun handleOnBackPressed() {
            // 戻るキーを無効にする＝処理を書かない
            return
        }
    }

    private fun singOut() {
        val myApp = applicationContext as MyApplication
        myApp.auth.signOut()
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}