package best.mobile.stenik.ui.screens.vocabulary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import best.mobile.entities.Utils.textStENikListToJsonStringArray
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.ButtonStENik
import best.mobile.stenik.ui.screens.vocabulary.custom_view.CardForVocabularyText
import best.mobile.stenik.ui.screens.vocabulary.custom_view.SearchTextStENik
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScreenVocabularyMain(
    auth: FirebaseAuth,
    viewModelVocabularyMain: ViewModelVocabularyMain = koinViewModel(),
    onClickEdit: (Array<String>) -> Unit,
    onClickCloud: () -> Unit,
    onClickCustom: () -> Unit
) {


    val textStENikListList = remember { viewModelVocabularyMain.textStENikListList}

    val showSearchIcon = remember { viewModelVocabularyMain.showSearchIcon }
    val searchText = remember { viewModelVocabularyMain.searchText }
    //Log.d(TAG_STENIK , "textStENikListList   " + textStENikListList)
    val iconIsEnable = remember { mutableStateOf(false) }

    val bottomPadding = remember { mutableStateOf(0.dp) }


    if (auth.currentUser != null) {
        iconIsEnable.value = true
    }

    Scaffold(
        bottomBar = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                ButtonStENik(text = stringResource(R.string.btb_create)) {
                    onClickEdit(viewModelVocabularyMain.getNewEmptyTextStENikList())
                }

                ButtonStENik(text = stringResource(R.string.btb_custom)) {
                    onClickCustom()
                }

                ButtonStENik(text = stringResource(R.string.btn_database)) {
                    onClickCloud()
                }

            }
        }
    ) { paddingValues ->
        bottomPadding.value = paddingValues.calculateBottomPadding()


        Column(
            modifier = Modifier.padding(bottom = bottomPadding.value),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (showSearchIcon.value)
                SearchTextStENik(
                    text = searchText.value,
                    label = "",
                    onValueChange = { newText ->
                        viewModelVocabularyMain.searchText.value = newText
                    },
                    onSearch = { newText ->
                        viewModelVocabularyMain.filteredTextStENikListList(newText)
                    }
                )

            LazyColumn(
                verticalArrangement = Arrangement.Top
            ) {

                items(
                    items = textStENikListList,
                    key = { item  -> item }

                ) { item  ->
                    CardForVocabularyText(textStENikList = item ) { getTextStENikList ->
                        // Для передачи между экранами сериализую массив в JSON-строку
                        val jsonStringArray =getTextStENikList.textStENikListToJsonStringArray()
                           // Utils.textStENikListToJsonStringArray(getTextStENikList)
                        onClickEdit(jsonStringArray)
                    }
                }
            }
        }
    }
}




