package com.sagirov.ilovedog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class BottomTabs (
    @StringRes
    val title: Int,
    @DrawableRes
    val icon: Int,
    val route: String
) {
    HOME(R.string.home , R.drawable.home_48px, "home"),
    KNOWLEDGES(R.string.knowledges, R.drawable.school_48px, "knowledges"),
    DOGCARDS(R.string.dogCards,R.drawable.menu_book_48px, "dogcards"),
    HEALTH(R.string.health,R.drawable.health_and_safety_48px,"health"),
    MENU(R.string.menu, R.drawable.menu_48px, "menu")
}