package best.mobile.stenik.ui.navigation.navgraph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import best.mobile.stenik.ui.screens.vocabulary.ScreenVocabularyNameCustom

fun NavGraphBuilder.vocabularyCustomNavGraph(
    onClickBack: () -> Unit
) {
    navigation(
        route = Graph.VOCABULARY_CUSTOM,
        startDestination = VocabularyCustomScreens.VocabularyCustom.route
    ) {
        composable(route = VocabularyCustomScreens.VocabularyCustom.route) {
            ScreenVocabularyNameCustom(){
                onClickBack()
            }
        }
    }
}

sealed class VocabularyCustomScreens(val route: String) {
    object VocabularyCustom : VocabularyCustomScreens(route = "VOCABULARY_CUSTOM")
}