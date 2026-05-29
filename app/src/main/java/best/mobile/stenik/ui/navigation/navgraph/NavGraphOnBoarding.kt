package best.mobile.stenik.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.ScreenOnBoarding

fun NavGraphBuilder.onBoardingNavGraph(navController: NavHostController) {

    navigation(
        route = Graph.ONBOARDING,
        startDestination = OnBoardingScreen.OnBoarding.route
    ) {
        composable(route = OnBoardingScreen.OnBoarding.route) {

            ScreenOnBoarding(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                }
            )


        }
    }
}

sealed class OnBoardingScreen(val route: String) {
    object OnBoarding : OnBoardingScreen(route = "ONBOARDING")
}