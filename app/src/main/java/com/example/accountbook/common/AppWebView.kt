package com.example.accountbook.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import android.webkit.WebViewClient

class AppWebView : WebView {
    constructor(context: Context) : super(context) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWebView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initWebView()
    }

    private fun initWebView() {
        // JavaScriptを有効化
        settings.javaScriptEnabled = true
        // WebView内でのページ遷移を可能にする
        webViewClient = WebViewClient()
        // 縦スクロールバーを有効化
        isVerticalScrollBarEnabled = true
        // スクロールバーのスタイルを設定
        scrollBarStyle = WebView.SCROLLBARS_INSIDE_OVERLAY
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // タッチイベントを処理して、親のスクロールビューにイベントを伝達しないようにする
        if (event != null && event.action == MotionEvent.ACTION_MOVE) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(event)
    }
}