package best.mobile.stenik.ui.navigation.bottombar

import best.mobile.stenik.R

sealed class BottomBarItems(
    val route: String,
    val title: Int,
    val icon: Int
) {
    object Home : BottomBarItems(
        route = "HOME",
        title =  R.string.btn_home,
        icon = R.drawable.iconhome
    )

    object Settings : BottomBarItems(
        route = "TEST",
        title = R.string.lbl_layoutTest,
        icon = R.drawable.ic_testing2
    )

    object Listen : BottomBarItems(
        route = "LISTEN",
        title = R.string.btn_listen,
        icon = R.drawable.ic_play
    )
}