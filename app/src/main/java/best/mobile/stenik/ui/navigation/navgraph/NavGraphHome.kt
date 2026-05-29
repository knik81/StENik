package best.mobile.stenik.ui.navigation.navgraph

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import best.mobile.entities.Utils.JSON_ARRAY_STRING_TO_OTHER_SCREEN
import best.mobile.stenik.R
import best.mobile.stenik.ui.navigation.bottombar.BottomBarItems
import best.mobile.stenik.ui.screens.authentification.ViewModelAuthentification
import best.mobile.stenik.ui.screens.custom_view.LayoutTop
import best.mobile.stenik.ui.screens.home.ScreenHome
import best.mobile.stenik.ui.screens.home.ViewModelHome
import best.mobile.stenik.ui.screens.listen.ScreenListen
import best.mobile.stenik.ui.screens.settings.ScreenSettings
import best.mobile.stenik.ui.screens.vocabulary.ScreenVocabularyTextEdit
import best.mobile.stenik.ui.theme.MyAppTheme
import org.koin.androidx.compose.koinViewModel
import kotlin.Unit

//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnrememberedGetBackStackEntry")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavGraphHome(
    viewModelAuthentification: ViewModelAuthentification = koinViewModel(),
    viwModelHome: ViewModelHome = koinViewModel(),
    navController: NavHostController
) {
    val auth = viewModelAuthentification.authMutable//remember { mutableStateOf(Firebase.auth) }
    val isRunAuthentification = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val layoutLabel = remember { mutableStateOf(context.getString(R.string.lbl_layoutHome)) }
    val accountIconId = remember { mutableIntStateOf(R.drawable.ic_account_outline) }
    val showIconBack = remember { mutableStateOf(false) }
    val showAccountIcon = remember { mutableStateOf(true) }
    val isLoading = remember { mutableStateOf(false) }

    val isLaunchListen = remember { mutableStateOf(viwModelHome.getLaunchListen()) }

    MyAppTheme {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
        ) {

            if (auth.value.currentUser == null)
                accountIconId.intValue = R.drawable.ic_account_outline
            else
                accountIconId.intValue = R.drawable.ic_account


            //верхняя строчка со стрелкой назад, названием экрана, иконкой аккаунта
            LayoutTop(
                label = layoutLabel.value,
                isBack = showIconBack.value,
                accountIconId = accountIconId.intValue,
                showAccountIcon = showAccountIcon.value,
                onClickBack = {
                    isRunAuthentification.value = false
                    showAccountIcon.value = true
                    navController.popBackStack()
                },
                onClickAuthentification = {
                    layoutLabel.value =
                        context.getString(R.string.lbl_layoutAuthentification)
                    showIconBack.value = true
                    showAccountIcon.value = false
                    navController.navigate(Graph.AUTH)
                }
            )

            //переходы для ботомБар
            NavHost(
                navController = navController,
                route = Graph.HOME,
                startDestination = BottomBarItems.Home.route
            ) {


                //Home экран
                composable(route = BottomBarItems.Home.route) {
                    DoNotRecomposeStENik {
                        layoutLabel.value = context.getString(R.string.lbl_layoutHome)
                        showIconBack.value = false
                    }

                    ScreenHome(
                        auth = auth,
                        isRunAuthentification = isRunAuthentification.value,
                        isLaunchListen = isLaunchListen.value,
                        onClickVocabulary = {
                            layoutLabel.value = context.getString(R.string.lbl_layoutVocabulary)
                            showIconBack.value = true
                            navController.navigate(Graph.VOCABULARY)
                        },
                        onClickSettings = {
                            layoutLabel.value = context.getString(R.string.lbl_layoutSettings)
                            showIconBack.value = true
                            navController.navigate(Graph.SETTINGS)
                        },
                        onClickListen = { navController.navigate(BottomBarItems.Listen.route) },
                        onClickAuthentification = {
                            layoutLabel.value =
                                context.getString(R.string.lbl_layoutAuthentification)
                            showIconBack.value = true
                            navController.navigate(Graph.AUTH)
                        },
                        changeIsLaunchListen = {
                            isLaunchListen.value = it
                        }
                    )
                }

                //Словарь основной
                vocabularyNavGraph(
                    auth.value,
                    onClickEdit = { jsonStringArray ->
                        layoutLabel.value =
                            context.getString(R.string.lbl_layoutVocabularyEdit)
                        showIconBack.value = true
                        //заполнение данными для передачи в другой экран (VOCABULARY_EDIT)
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            JSON_ARRAY_STRING_TO_OTHER_SCREEN,
                            jsonStringArray
                        )
                        navController.navigate(Graph.VOCABULARY_EDIT)
                    },
                    onClickCustom = {
                        layoutLabel.value =
                            context.getString(R.string.lbl_layoutVocabularyCustom)
                        showIconBack.value = true
                        navController.navigate(Graph.VOCABULARY_CUSTOM)
                    },

                    onClickCloud = {
                        layoutLabel.value =
                            context.getString(R.string.lbl_database)
                        showIconBack.value = true
                        navController.navigate(Graph.VOCABULARY_CLOUD)
                    },
                )


                /*
            composable(
                route = BottomBarItems.Vocabulary.route,
            ) {

                DoNotRecomposeStENik() {
                    layoutLabel.value = context.getString(R.string.lbl_layoutVocabulary)
                    showIconBack.value = false
                }
                ScreenVocabulary(
                    auth.value,
                    onClickEdit = { jsonStringArray ->
                        layoutLabel.value =
                            context.getString(R.string.lbl_layoutVocabularyEdit)
                        showIconBack.value = true
                        //заполнение данными для передачи в другой экран (VOCABULARY_EDIT)
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            JSON_ARRAY_STRING_TO_OTHER_SCREEN,
                            jsonStringArray
                        )
                        navController.navigate(Graph.VOCABULARY_EDIT)
                    },
                    onClickCustom = {
                        layoutLabel.value =
                            context.getString(R.string.lbl_layoutVocabularyCustom)
                        showIconBack.value = true
                        navController.navigate(Graph.VOCABULARY_CUSTOM)
                    },

                    onClickCloud = {
                        layoutLabel.value =
                            context.getString(R.string.lbl_database)
                        showIconBack.value = true
                        navController.navigate(Graph.VOCABULARY_CLOUD)
                    },
                ) //ScreenVocabularyCloud(auth.value)
            }

             */


                //Словарь - редактирование
                composable(
                    route = Graph.VOCABULARY_EDIT
                ) {
                    //получаю данные из предыдущего экрана
                    val jsonStringArray: Array<String>? =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Array<String>>(
                            JSON_ARRAY_STRING_TO_OTHER_SCREEN
                        )

                    if (!jsonStringArray.isNullOrEmpty())
                    //передаю загруженные данные в экран
                        ScreenVocabularyTextEdit(
                            jsonStringArray = jsonStringArray,
                            onClickBack = {
                                isRunAuthentification.value = false
                                showAccountIcon.value = true
                                navController.popBackStack()
                            }
                        )
                }


                //Словарь - настройка
                vocabularyCustomNavGraph() {
                    navController.popBackStack()
                }


                //Словарь - облако
                vocabularyCloudNavGraph(
                    auth = auth.value,
                    onClickBack = { navController.popBackStack() },
                    isLoadingChange = { isLoading.value = it }
                )


                //Настройка

                composable(route = Graph.SETTINGS) {
                    // composable(route = BottomBarItems.Settings.route) {
                    DoNotRecomposeStENik {
                        layoutLabel.value = context.getString(R.string.lbl_layoutSettings)
                        showIconBack.value = true
                    }

                    ScreenSettings(
                        onClickExtra = {
                            layoutLabel.value =
                                context.getString(R.string.lbl_layoutSettingsExtra)
                            showIconBack.value = true
                            navController.navigate(Graph.SETTINGS_EXTRA)
                        }
                    )
                }

                //Настройка - дополнительно
                settingsExtraNavGraph()


                //Слушать
                composable(route = BottomBarItems.Listen.route) {
                    DoNotRecomposeStENik {
                        layoutLabel.value = context.getString(R.string.lbl_layoutListen)
                        showIconBack.value = false
                    }
                    ScreenListen()
                }


                //Аутентификация
                authentificationNavGraph(
                    auth = auth,
                    isRunAuthentificationChange = { isRunAuthentification.value = it },
                    onClickBack = {
                        showAccountIcon.value = true
                        navController.popBackStack()
                    }
                )

                testNavGraph()

            }
        }

        //Блокировка экрана в момент загрузки
        if (isLoading.value)
            LoadingBox(isLoading.value)
        else
            LoadingBox(isLoading.value)
    }

}


@Composable
private fun DoNotRecomposeStENik(
    change: () -> Unit
) {
    change()
}

@Composable
fun LoadingBox(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {}
                .background(Color.Gray.copy(alpha = 0.3f))
                .clickable(enabled = false, onClick = { })
                .then(Modifier.wrapContentSize(Alignment.Center)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
        }
    }
}