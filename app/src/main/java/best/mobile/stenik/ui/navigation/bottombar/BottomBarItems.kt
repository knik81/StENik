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
/*
    object Vocabulary : BottomBarItems(
        route = "VOCABULARY",
        title = R.string.btn_vocabulary,
        icon = R.drawable.iconvocabular
    )

 */

    object Settings : BottomBarItems(
        route = "TEST",
        title = R.string.lbl_layoutTest,
        icon = R.drawable.ic_testing2
    )
/*
    object Settings : BottomBarItems(
        route = "SETTINGS",
        title = R.string.btn_settings,
        icon = R.drawable.iconsettings
    )

 */

    object Listen : BottomBarItems(
        route = "LISTEN",
        title = R.string.btn_listen,
        icon = R.drawable.ic_play
    )
}