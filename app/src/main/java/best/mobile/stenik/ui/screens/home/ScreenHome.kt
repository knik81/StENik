package best.mobile.stenik.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import best.mobile.stenik.R
import best.mobile.stenik.ui.screens.custom_view.ButtonStENik
import com.google.firebase.auth.FirebaseAuth

@Composable

fun ScreenHome(
    auth: MutableState<FirebaseAuth>,
    isRunAuthentification: Boolean,
    isLaunchListen: Boolean,
    onClickSettings: () -> Unit,
    onClickListen: () -> Unit,
    onClickVocabulary: () -> Unit,
    onClickAuthentification: () -> Unit,
    changeIsLaunchListen: (Boolean) -> Unit
) {

    val isLaunchListen = remember { mutableStateOf(isLaunchListen)}
    val scrollState = rememberScrollState()
    if (auth.value.currentUser == null && isRunAuthentification) {
        onClickAuthentification()
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {


        Spacer(modifier = Modifier.padding(50.dp))


        ButtonStENik(
            text = stringResource(R.string.lbl_layoutSettings),
            onClick = {
                onClickSettings()
            }
        )

        ButtonStENik(
            text = stringResource(R.string.lbl_layoutVocabulary),
            onClick = {
                onClickVocabulary()
            }
        )


        ButtonStENik(
            text = stringResource(R.string.lbl_layoutListen),
            onClick = {
                onClickListen()
            }
        )

        Spacer(modifier = Modifier.padding(50.dp))


        //автостарт прослушивания, если пользователь активировал в настройке
        LaunchedEffect(2) {
            if (isLaunchListen.value)
                onClickListen()
            changeIsLaunchListen(false)
        }
    }
}