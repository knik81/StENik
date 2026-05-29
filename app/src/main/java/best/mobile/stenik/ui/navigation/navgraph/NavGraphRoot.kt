package best.mobile.stenik.ui.navigation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import best.mobile.stenik.ui.navigation.RootScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavigationGraphRoot(navController: NavHostController) {
            NavHost(
                navController = navController,
                route = Graph.ROOT,
                startDestination = Graph.ONBOARDING
            ) {
                onBoardingNavGraph(navController = navController)
                composable(route = Graph.HOME) {
                    RootScreen()
                }
            }
}

