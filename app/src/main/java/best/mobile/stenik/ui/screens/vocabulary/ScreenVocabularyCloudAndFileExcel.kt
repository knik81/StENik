package best.mobile.stenik.ui.screens.vocabulary

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.ButtonStENik
import best.mobile.stenik.ui.screens.custom_view.PopupStENik
import best.mobile.stenik.ui.screens.custom_view.showResultStENik
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@SuppressLint("ConfigurationScreenWidthHeight", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenVocabularyCloudAndFileExcel(
    auth: FirebaseAuth,
    viewModelVocabularyMain: ViewModelVocabularyMain = koinViewModel(),
    onClickBack: () -> Unit,
    isLoadingChange: (Boolean) -> Unit
) {
    val isLogin = remember { mutableStateOf(false) }
    val openPopup = remember { mutableStateOf(false) }
    //val screenFilePicker = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val selectedFileUri = remember { mutableStateOf<Uri?>(null) }
    // Запускаем ActivityResultLauncher для выбора файла
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        selectedFileUri.value = uri
    }


    isLogin.value = auth.currentUser != null


    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.padding(vertical = 50.dp))

            if (isLogin.value) {
                ButtonStENik(
                    text = stringResource(R.string.btb_loadFromCloud),
                    onClick = {
                        openPopup.value = true
                    }
                )

                Spacer(modifier = Modifier.padding(50.dp))

                ButtonStENik(
                    text = stringResource(R.string.btb_saveToCloud),
                    onClick = {
                        isLoadingChange(true)
                        viewModelVocabularyMain.saveTextStenikEntityInFireStore(auth = auth) { resultStENik ->
                            showResultStENik(context = context, resultStENik = resultStENik)
                            isLoadingChange(false)
                            onClickBack()
                        }

                    }
                )

                Spacer(modifier = Modifier.padding(50.dp))

                //selectedFileUri = null
                ButtonStENik(
                    text = stringResource(R.string.btb_loadExcel),
                    onClick = {
                        launcher.launch( //выбор Excel файла
                            arrayOf(
                                //"*/*"
                                "application/vnd.ms-excel",
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            )
                        )
                    }
                )
                //Если файл выбран, то загрузка данные
                if (selectedFileUri.value != null) {
                    isLoadingChange(true)
                    viewModelVocabularyMain.loadFromExcelFile(
                        context = context,
                        uri = selectedFileUri.value!!,
                        callBack = { resultStENik ->
                            onClickBack()
                            isLoadingChange(false)
                            showResultStENik(context = context, resultStENik = resultStENik)
                        })
                }


                Spacer(modifier = Modifier.padding(50.dp))
            } else {
                Text(
                    text = stringResource(R.string.txt_noLogin),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 50.dp))


        }
    }

    //Всплывающее окно
    if (openPopup.value) {
        PopupStENik(
            setLabel = stringResource(R.string.txt_attention) + "\n" + stringResource(R.string.txt_replaceFromCloud),
            openPopup = openPopup,
            onClickYes = {
                isLoadingChange(true)
                viewModelVocabularyMain.loadFromFireStore(
                    auth = auth,
                    callBack = { resultStENik ->
                        isLoadingChange(false)
                        showResultStENik(context = context, resultStENik = resultStENik)
                        onClickBack()
                    })
                openPopup.value = false

            }
        )
    }

}











