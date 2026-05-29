package best.mobile.stenik.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.test.ScreenTest

fun NavGraphBuilder.testNavGraph() {
    navigation(
        route = Graph.TEST,
        startDestination = TestScreens.Test.route
    ) {
        composable(route = TestScreens.Test.route) {
            ScreenTest()
        }
    }
}

sealed class TestScreens(val route: String) {
    object Test : TestScreens(route = "TEST")
}