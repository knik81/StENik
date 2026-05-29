package best.mobile.stenik.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.settings.ScreenSettings

fun NavGraphBuilder.settings(
    onClickExtra: () -> Unit,
) {
    navigation(
        route = Graph.SETTINGS,
        startDestination = SettingsScreens.Settings.route
    ) {
        composable(route = SettingsScreens.Settings.route) {
            ScreenSettings{
                onClickExtra()
            }
        }
    }
}

sealed class SettingsScreens(val route: String) {
    object Settings : SettingsScreens(route = "SETTINGS")
}