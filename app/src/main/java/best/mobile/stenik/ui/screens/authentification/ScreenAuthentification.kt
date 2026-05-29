package best.mobile.stenik.ui.screens.authentification


import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import best.mobile.entities.ResultStENik
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.TextFieldAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScreenAuthentification(
    viewModelAuthentification: ViewModelAuthentification = koinViewModel(),
    auth: MutableState<FirebaseAuth>,
    onClickBack: (Boolean) -> Unit
) {

    val currentUser = remember { mutableStateOf<FirebaseUser?>(null) }
    val message = remember { mutableStateOf("") }
    val email =
        remember {
            mutableStateOf(
                if (auth.value.currentUser != null)
                    auth.value.currentUser?.email.toString()
                else ""
            )
        }
    val password = remember { mutableStateOf("") }
    val scrollStateScreen: ScrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollStateScreen)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top

        ) {
            Spacer(Modifier.height(80.dp))

            TextFieldAuth(
                text = email.value,
                label = "Email"
            ) { email.value = it }

            Spacer(Modifier.height(30.dp))

            TextFieldAuth(
                text = password.value,
                label = "Password"
            ) { password.value = it }

            Spacer(Modifier.height(30.dp))


            //Универсальная функция для применения результата
            fun applyResultStENik(resultStENik: ResultStENik<FirebaseUser>) {
                when (resultStENik) {
                    is ResultStENik.Success -> {
                        currentUser.value = resultStENik.data
                        onClickBack(false)
                    }

                    is ResultStENik.Error -> {
                        message.value = resultStENik.message
                    }
                }
            }

            //Выбор функций
            //если в аккаунте - то можно выйти или удалить
            //если НЕ вошел в аккаунт - можно создать аккаунт или войти
            if (auth.value.currentUser == null) {

                //Вход в аккаунт
                Button(onClick = {
                    message.value = ""
                    viewModelAuthentification.signIn(
                        auth = auth.value,
                        email = email.value,
                        password = password.value,
                        callBack = { resultStENik ->
                            applyResultStENik(resultStENik)
                        })
                }) {
                    Text(text = stringResource(R.string.btn_signIn))
                }

                Spacer(Modifier.height(30.dp))

                //Создание аккаунта
                Button(
                    onClick = {
                        message.value = ""
                        viewModelAuthentification.signUp(
                            auth = auth.value,
                            email = email.value,
                            password = password.value,
                            callBack = { resultStENik ->
                                applyResultStENik(resultStENik)
                            })
                    }) {
                    Text(text = stringResource(R.string.btn_signUp))
                }

            } else {

                //Выйти из аккаунта
                Button(
                    onClick = {
                        message.value = ""
                        viewModelAuthentification.signOut(
                            auth = auth.value,
                            callBack = { resultStENik ->
                                applyResultStENik(resultStENik)
                            })
                    }) {
                    Text(text = stringResource(R.string.btn_signOut))
                }
                Spacer(Modifier.height(30.dp))

                //Удалить аккаунт
                Button(
                    onClick = {
                        message.value = ""
                        viewModelAuthentification.signDelete(
                            auth = auth.value,
                            callBack = { resultStENik ->
                                applyResultStENik(resultStENik)
                            })
                    }) {
                    Text(text = stringResource(R.string.btn_signDelete))
                }
            }

            Spacer(Modifier.height(10.dp))

            //Текст с результатом
            Box(
                modifier = Modifier.fillMaxWidth(0.6f),
            ) {
                Text(
                    style = TextStyle(textAlign = TextAlign.Center),
                    text = message.value,
                    color = MaterialTheme.colorScheme.error //colorTxt,
                )
            }
        }
    }


}

