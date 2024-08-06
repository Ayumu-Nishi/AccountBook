package com.example.accountbook

import com.example.accountbook.Model.ActionBarConfig
import com.example.accountbook.Model.ActionBarDisplayMode
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

abstract class ParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActionBar()
    }

    private fun setActionBar() {
        setContentView(R.layout.activity_parent)

        // ActionBarをカスタムする
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setCustomView(R.layout.actionbar_custom)
        }

        // カスタムActionBarのビューを取得
        val actionBarView = supportActionBar?.customView

        // ActionBarの設定を取得
        val actionBarConfig = getActionBarConfig()

        // タイトルの設定
        actionBarView?.findViewById<TextView>(R.id.title_text)?.text = actionBarConfig.title

        // 左側のボタンの設定
        when (actionBarConfig.leftDisplayMode) {
            ActionBarDisplayMode.ICON -> {
                // 左側のイベントの設定
                actionBarView?.findViewById<ImageView>(R.id.left_icon_image)?.setOnClickListener {
                    onLeftContainerClicked()
                }
                // ImageViewの表示
                actionBarView?.findViewById<ImageView>(R.id.left_icon_image)?.visibility =
                    View.VISIBLE
                // Imageの設定
                actionBarView?.findViewById<ImageView>(R.id.left_icon_image)
                    ?.setImageResource(actionBarConfig.leftIconResId ?: R.drawable.arrow_back_40)
                // TextViewの非表示
                actionBarView?.findViewById<TextView>(R.id.left_icon_text)?.visibility = View.GONE
            }

            ActionBarDisplayMode.TEXT -> {
                // 左側のイベントの設定
                actionBarView?.findViewById<TextView>(R.id.left_icon_text)?.setOnClickListener {
                    onLeftContainerClicked()
                }
                // TextViewの表示
                actionBarView?.findViewById<TextView>(R.id.left_icon_text)?.visibility = View.VISIBLE
                // Textの設定
                actionBarView?.findViewById<TextView>(R.id.left_icon_text)?.text = actionBarConfig.leftText ?: getString(R.string.actionBar_leftButtonTitle)
                // imageViewの非表示
                actionBarView?.findViewById<ImageView>(R.id.left_icon_image)?.visibility = View.GONE
            }

            ActionBarDisplayMode.NONE -> {
                // ImageView、TextViewの非表示
                actionBarView?.findViewById<ImageView>(R.id.left_icon_image)?.visibility = View.GONE
                actionBarView?.findViewById<TextView>(R.id.left_icon_text)?.visibility = View.GONE
            }
        }

        // 右側のボタンの設定
        when (actionBarConfig.rightDisplayMode) {
            ActionBarDisplayMode.ICON -> {
                // 右側のイベントの設定
                actionBarView?.findViewById<ImageView>(R.id.right_icon_image)?.setOnClickListener {
                    onRightContainerClicked()
                }
                // ImageViewの表示
                actionBarView?.findViewById<ImageView>(R.id.right_icon_image)?.visibility =
                    View.VISIBLE
                // Imageの設定
                actionBarView?.findViewById<ImageView>(R.id.right_icon_image)
                    ?.setImageResource(actionBarConfig.rightIconResId ?: R.drawable.logout)
                // TextViewの非表示
                actionBarView?.findViewById<TextView>(R.id.right_icon_text)?.visibility = View.GONE
            }

            ActionBarDisplayMode.TEXT -> {
                // 右側のイベントの設定
                actionBarView?.findViewById<TextView>(R.id.right_icon_text)?.setOnClickListener {
                    onRightContainerClicked()
                }
                // TextViewの表示
                actionBarView?.findViewById<TextView>(R.id.right_icon_text)?.visibility = View.VISIBLE
                // Textの設定
                actionBarView?.findViewById<TextView>(R.id.right_icon_text)?.text = actionBarConfig.rightText ?: getString(R.string.actionBar_rightButtonTitle)
                // imageViewの非表示
                actionBarView?.findViewById<ImageView>(R.id.right_icon_image)?.visibility = View.GONE
            }

            ActionBarDisplayMode.NONE -> {
                // ImageView、TextViewの非表示
                actionBarView?.findViewById<ImageView>(R.id.right_icon_image)?.visibility = View.GONE
                actionBarView?.findViewById<TextView>(R.id.right_icon_text)?.visibility = View.GONE
            }
        }
    }

    // 左側のアイコンがクリックされたときの処理
    open fun onLeftContainerClicked() {
        // デフォルトでは何もしない
    }

    // 右側のアイコンがクリックされたときの処理
    open fun onRightContainerClicked() {
        // デフォルトでは何もしない
    }

    // ActionBarの設定を取得する抽象メソッド
    abstract fun getActionBarConfig(): ActionBarConfig
}