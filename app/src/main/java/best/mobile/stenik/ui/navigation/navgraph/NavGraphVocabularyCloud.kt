package best.mobile.stenik.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.vocabulary.ScreenVocabularyCloudAndFileExcel
import com.google.firebase.auth.FirebaseAuth

fun NavGraphBuilder.vocabularyCloudNavGraph(
    auth: FirebaseAuth,
    onClickBack: () -> Unit,
    isLoadingChange: (Boolean) -> Unit
) {
    navigation(
        route = Graph.VOCABULARY_CLOUD,
        startDestination = CloudScreens.Cloud.route
    ) {
        composable(route = CloudScreens.Cloud.route) {
            ScreenVocabularyCloudAndFileExcel(
                auth = auth,
                onClickBack = {
                    onClickBack()
                },
                isLoadingChange = { isLoadingChange(it) }
            )
        }
    }
}

sealed class CloudScreens(val route: String) {
    object Cloud : CloudScreens(route = "CLOUD")
}