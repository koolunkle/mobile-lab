package com.example.musicui

import androidx.annotation.DrawableRes

sealed class Screen(
    val title: String,
    val route: String,
) {
    sealed class BottomScreen(
        val bottomTitle: String,
        val bottomRoute: String,
        @DrawableRes val icon: Int,
    ) : Screen(bottomTitle, bottomRoute) {
        data object Home : BottomScreen(
            bottomTitle = "Home",
            bottomRoute = "home",
            icon = R.drawable.music_video_24,
        )

        data object Library : BottomScreen(
            bottomTitle = "Library",
            bottomRoute = "library",
            icon = R.drawable.video_library_24,
        )

        data object Browse : BottomScreen(
            bottomTitle = "Browse",
            bottomRoute = "browse",
            icon = R.drawable.apps_24,
        )
    }

    sealed class DrawerScreen(
        val drawerTitle: String,
        val drawerRoute: String,
        @DrawableRes val icon: Int,
    ) : Screen(drawerTitle, drawerRoute) {
        data object Account : DrawerScreen(
            drawerTitle = "Account",
            drawerRoute = "account",
            icon = R.drawable.account,
        )

        data object Subscription : DrawerScreen(
            drawerTitle = "Subscription",
            drawerRoute = "subscription",
            icon = R.drawable.subscribe,
        )

        data object AddAccount : DrawerScreen(
            drawerTitle = "Add Account",
            drawerRoute = "add_account",
            icon = R.drawable.person_add_alt_1_24,
        )
    }
}

val screensInBottom = listOf(
    Screen.BottomScreen.Home,
    Screen.BottomScreen.Library,
    Screen.BottomScreen.Browse,
)

val screensInDrawer = listOf(
    Screen.DrawerScreen.Account,
    Screen.DrawerScreen.Subscription,
    Screen.DrawerScreen.AddAccount,
)