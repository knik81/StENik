package best.mobile.stenik.ui.navigation.navgraph

/*
fun NavGraphBuilder.vocabularyEditNavGraph(
    navController: NavController
    //textStENikList: List<TextStENik>
) {
    navigation(
        route = Graph.VOCABULARY_EDIT,
        startDestination = VocabularyEditScreens.VocabularyEditEdit.route
    ) {
        val textStENikList =  mutableStateOf(listOf<TextStENik>())
        composable(route = VocabularyEditScreens.VocabularyEditEdit.route) {
            ScreenVocabularyEdit (//navController
                textStENikList.value
            )
        }

    }
}

sealed class VocabularyEditScreens(val route: String) {
    object VocabularyEditEdit : VocabularyEditScreens(route = "VOCABULARY_EDIT")
}


fun navigateWithArray(navController: NavController, jsonStringArray: Array<String>) {
    navController.currentBackStackEntry?.savedStateHandle?.set("jsonStringArray", jsonStringArray)
    navController.navigate(Graph.VOCABULARY_EDIT)
}
*/