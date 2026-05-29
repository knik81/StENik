package best.mobile.stenik.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.vocabulary.ScreenVocabularyMain
import com.google.firebase.auth.FirebaseAuth

fun NavGraphBuilder.vocabularyNavGraph(
    auth: FirebaseAuth,
    onClickEdit: (Array<String>) -> Unit,
    onClickCloud: () -> Unit,
    onClickCustom: () -> Unit
) {
    navigation(
        route = Graph.VOCABULARY,
        startDestination = VocabularyScreens.Vocabulary.route
    ) {
        composable(route = VocabularyScreens.Vocabulary.route) {
            ScreenVocabularyMain(
                auth = auth,
                onClickEdit = { jsonStringArray ->
                    onClickEdit(jsonStringArray)
                },
                onClickCloud = { onClickCloud() },
                onClickCustom = { onClickCustom() }
            )
        }
    }
}

sealed class VocabularyScreens(val route: String) {
    object Vocabulary : VocabularyScreens(route = "VOCABULARY")
}