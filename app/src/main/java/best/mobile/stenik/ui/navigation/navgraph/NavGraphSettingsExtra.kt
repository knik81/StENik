package best.mobile.stenik.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.settings.ScreenSettingsExtra

fun NavGraphBuilder.settingsExtraNavGraph() {
    navigation(
        route = Graph.SETTINGS_EXTRA,
        startDestination = SettingsExtraScreens.SettingsExtra.route
    ) {
        composable(route = SettingsExtraScreens.SettingsExtra.route) {
            ScreenSettingsExtra ()
        }
    }
}

sealed class SettingsExtraScreens(val route: String) {
    object SettingsExtra : SettingsExtraScreens(route = "SETTINGS_EXTRA")
}