package best.mobile.stenik.ui.navigation.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarItems.Home,
        BottomBarItems.Settings,
        BottomBarItems.Listen
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEachIndexed { _ , item ->
                NavigationBarItem(
                    label = {
                        Text(text = stringResource(item.title))
                    },
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(item.icon),
                            contentDescription = "Navigation Icon"
                        )
                    },
                    selected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true,

                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }

}
