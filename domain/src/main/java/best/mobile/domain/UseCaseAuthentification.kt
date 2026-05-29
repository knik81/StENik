package best.mobile.domain

import best.mobile.data.firebase.Authentification
import best.mobile.entities.AuthAnswer
import best.mobile.entities.ResultStENik
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UseCaseAuthentification(
    private val authentification: Authentification,
) {

    fun <T> signUp(
        auth: FirebaseAuth,
        email: String,
        password: String,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        authentification.signUp(
            auth = auth,
            email = email,
            password = password,
            callBack = { resultStENik ->
                callBack(resultStENik)
            })
    }

    fun <T> signIn(
        auth: FirebaseAuth,
        email: String,
        password: String,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        authentification.signIn(
            auth = auth,
            email = email,
            password = password,
            callBack = { resultStENik ->
                callBack(resultStENik)
            })
    }

    fun <T> signOut(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        authentification.signOut(
            auth = auth,
            callBack = { resultStENik ->
                callBack(resultStENik)
            })
    }


    fun <T> signDelete(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        authentification.signDelete(
            auth = auth,
            callBack = { resultStENik ->
                callBack(resultStENik)
            })
    }


    fun getAuth() = authentification.getAuth()


}




