package best.mobile.stenik.ui.navigation.navgraph

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.authentification.ScreenAuthentification
import com.google.firebase.auth.FirebaseAuth

fun NavGraphBuilder.authentificationNavGraph(
    auth: MutableState<FirebaseAuth>,
    isRunAuthentificationChange: (Boolean) -> Unit,
    onClickBack: () -> Unit,
) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthentificationScreens.Authentification.route
    ) {
        composable(route = AuthentificationScreens.Authentification.route) {
            ScreenAuthentification(auth = auth) {
                isRunAuthentificationChange(false)
                onClickBack()
            }
        }

    }
}

sealed class AuthentificationScreens(val route: String) {
    object Authentification : AuthentificationScreens(route = "AUTH")
}