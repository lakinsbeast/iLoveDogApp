package com.sagirov.ilovedog

import androidx.annotation.DrawableRes

enum class BottomTabs(
    @DrawableRes
    val icon: Int,
    val route: String
) {
    HOME(R.drawable.home_48px, "home"),
    KNOWLEDGES(R.drawable.school_48px, "knowledges"),
    DOGCARDS(R.drawable.menu_book_48px, "dogcards"),
    HEALTH(R.drawable.health_and_safety_48px, "health"),
    MENU(R.drawable.menu_48px, "menu")
}