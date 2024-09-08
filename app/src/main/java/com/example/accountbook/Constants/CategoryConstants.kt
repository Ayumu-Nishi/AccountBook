package com.example.accountbook.Constants

import com.example.accountbook.R

object CategoryConstants {

    // 支出カテゴリ
    val expensesCategories = mapOf(
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

    // 収入カテゴリ
    val incomeCategories = mapOf(
        1 to "給料",
        2 to "お小遣い",
        3 to "年金",
        4 to "立替",
        5 to "不明",
        6 to "その他"
    )

    // 支出アイコン
    val expensesIcons = mapOf(
        1 to R.drawable.ic_restaurant_24,
        2 to R.drawable.ic_household_24,
        3 to R.drawable.ic_hiking_24,
        4 to R.drawable.ic_diversity_24,
        5 to R.drawable.ic_commute_24,
        6 to R.drawable.ic_apparel_24,
        7 to R.drawable.ic_cardiology_24,
        8 to R.drawable.ic_airport_shuttle_24,
        9 to R.drawable.ic_school_24,
        10 to R.drawable.ic_star_rate_24,
        11 to R.drawable.ic_wb_incandescent_24,
        12 to R.drawable.ic_cell_wifi_24,
        13 to R.drawable.ic_house_24,
        14 to R.drawable.ic_paid_24,
        15 to R.drawable.ic_health_and_safety_24,
        16 to R.drawable.ic_label_24
    )

    // 収入アイコン
    val incomeIcons = mapOf(
        1 to R.drawable.ic_work_24,
        2 to R.drawable.ic_child_care_24,
        3 to R.drawable.ic_stairs_24,
        4 to R.drawable.ic_currency_exchange_24,
        5 to R.drawable.ic_question_mark_24,
        6 to R.drawable.ic_label_empty_24
    )
}