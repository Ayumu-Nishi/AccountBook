package com.example.accountbook.Model

data class ActionBarConfig(
    val title: String = "",
    val leftDisplayMode: ActionBarDisplayMode = ActionBarDisplayMode.NONE,
    val leftIconResId: Int?,
    val leftText: String?,
    val rightDisplayMode: ActionBarDisplayMode = ActionBarDisplayMode.NONE,
    val rightIconResId: Int?,
    val rightText: String?
)

enum class ActionBarDisplayMode {
    ICON,
    TEXT,
    NONE
}