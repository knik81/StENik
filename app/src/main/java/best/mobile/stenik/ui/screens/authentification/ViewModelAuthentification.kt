package best.mobile.stenik.ui.screens.authentification

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import best.mobile.domain.UseCaseAuthentification
import best.mobile.entities.ResultStENik
import com.google.firebase.auth.FirebaseAuth

class ViewModelAuthentification(private val useCaseAuthentification: UseCaseAuthentification) :
    ViewModel() {


    private val _authMutable = useCaseAuthentification.getAuth()
    val authMutable: MutableState<FirebaseAuth>
        get() = mutableStateOf(_authMutable)

    fun <T> signUp(
        auth: FirebaseAuth,
        email: String,
        password: String,
        callBack: (ResultStENik<T>) -> Unit
    ) = useCaseAuthentification.signUp(
        auth = auth,
        email = email,
        password = password,
        callBack = { resultStENik ->
            callBack(resultStENik)
        }
    )

    fun <T> signIn(
        auth: FirebaseAuth,
        email: String,
        password: String,
        callBack: (ResultStENik<T>) -> Unit
    ) = useCaseAuthentification.signIn(
        auth = auth,
        email = email,
        password = password,
        callBack = { resultStENik ->
            callBack(resultStENik)
        })

    fun <T> signOut(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) = useCaseAuthentification.signOut(
        auth = auth,
        callBack = { resultStENik ->
            callBack(resultStENik)
        })

    fun <T> signDelete(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) = useCaseAuthentification.signDelete(
        auth = auth,
        callBack = { resultStENik ->
            callBack(resultStENik)
        })

    /*
    //Мэппинг сообщения из слоя data и текста из ресурсов string
    private fun getMessageById(messageId: Int, context: Context): String =
        when (messageId) {
            1 -> context.getString(R.string.msg_emailPassEmpty)
            2 -> context.getString(R.string.msg_createUserSuccess)
            3 -> context.getString(R.string.msg_createUserFailed)
            4 -> context.getString(R.string.msg_authSuccess)
            5 -> context.getString(R.string.msg_authFailed)
            6 -> context.getString(R.string.msg_outSuccess)
            7 -> context.getString(R.string.msg_outFailed)
            8 -> context.getString(R.string.msg_authDeleteSuccess)
            9 -> context.getString(R.string.msg_authDeleteFailed)
            else -> ""
        }

     */
}