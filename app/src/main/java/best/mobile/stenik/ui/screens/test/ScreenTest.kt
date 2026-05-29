package best.mobile.stenik.ui.screens.test

import android.annotation.SuppressLint
import android.app.Activity
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.LocalLifecycleOwner
import best.mobile.entities.Utils.TEMPORARY_ID
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.ButtonStENik
import best.mobile.stenik.ui.screens.custom_view.HorizontalDividerStENik
import best.mobile.stenik.ui.screens.test.custom_view.CardForTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ScreenTest(
    viewModelTest: ViewModelTest = koinViewModel()
) {

    val context = LocalContext.current
    val window = remember { (context as? Activity)?.window }

    val lifecycleOwner = LocalLifecycleOwner.current

    //первичная загрузка списка слов
    LaunchedEffect(Unit) {
        viewModelTest.updateTextStENikListListWithoutLanguageMain()
    }

    //Образец текста
    val textStENikLanguageMain = remember { viewModelTest.textStENikLanguageMain }

    //Список списков вариантов для выбора
    val textStENikListListWithoutLanguageMain =
        remember { viewModelTest.textStENikListListWithoutLanguageMain }

    val incorrectText = stringResource(R.string.mss_incorrect)
    val correctText = stringResource(R.string.mss_correct)

    //не гаснущий экран
    //KeepScreenOn()


    Scaffold(
        bottomBar = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalDividerStENik()
                ButtonStENik(text = stringResource(R.string.btb_donotknow)) {
                    viewModelTest.toCheck(
                        setId = TEMPORARY_ID,
                        textCorrect = "-",
                        textIncorrect = "",
                        next = true,
                        delay = 0L
                    ) { _, _ -> }

                }
                HorizontalDividerStENik()
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
        ) {
            Row {
                Box(modifier = Modifier.fillMaxWidth(.9F)) {
                    Text(
                        text = stringResource(R.string.txt_example),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                Text(text = textStENikLanguageMain.value?.level.toString())
            }


            if (textStENikLanguageMain.value != null)
                CardForTest(
                    textStENikList = listOf(textStENikLanguageMain.value!!),
                ) {}

            //HorizontalDividerStENik()
            HorizontalDividerStENik()

            Text(
                text = stringResource(R.string.txt_selectCorrect),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            HorizontalDividerStENik()

            LazyColumn(verticalArrangement = Arrangement.Top)
            {
                items(items = textStENikListListWithoutLanguageMain) { textStENikList ->
                    val initColor = MaterialTheme.colorScheme.surfaceContainer
                    val correctColor = Color.Green
                    val inCorrectColor = MaterialTheme.colorScheme.error

                    val dynamicColor = remember { mutableStateOf(initColor) }

                    val isCorrect = remember { mutableStateOf(false) }
                    val isInCorrect = remember { mutableStateOf(false) }

                    CardForTest(
                        textStENikList = textStENikList,
                        containerColor = dynamicColor.value
                    ) { getTextStENikList ->
                        val id = getTextStENikList.firstOrNull()?.id ?: 999
                        viewModelTest.toCheck(
                            setId = id,
                            textCorrect = correctText,
                            textIncorrect = incorrectText,
                        ) { message, getIsCorrect ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                .show()

                            if (!getIsCorrect)
                                isInCorrect.value = true
                            else
                                isCorrect.value = true

                        }
                    }

                    //Мерцание при неверном ответе
                    if (isInCorrect.value) {
                        rememberCoroutineScope().launch {
                            dynamicColor.value = inCorrectColor
                            delay(500L)
                            dynamicColor.value = initColor
                            isInCorrect.value = false
                        }
                    }

                    //Мерцание при верном ответе
                    if (isCorrect.value) {
                        rememberCoroutineScope().launch {
                            dynamicColor.value = correctColor
                            delay(500L)
                            dynamicColor.value = initColor
                            isCorrect.value = false
                        }
                    }

                }
            }
        }
    }



    //if (keepScreenOn.value)

    LaunchedEffect(1) {
        delay(10000)
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    //Жизненный цикл экрана
    DisposableEffect(lifecycleOwner) {
        //не гаснущий экран
        //window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Удаление наблюдателя при удалении Composable из композиции
        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        }
    }

}



