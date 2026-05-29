package best.mobile.data.firebase

import best.mobile.entities.ResultStENik
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Authentification {

    fun getAuth() = Firebase.auth


    fun <T>signUp(
        auth: FirebaseAuth,
        email: String,
        password: String,
        callBack: (ResultStENik<T>) -> Unit,
    ) {
        if (email.isEmpty() || password.isEmpty())
            callBack(ResultStENik.Error("Email or password is empty"))
        else
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Log.d(TAG_STENIK, "Create user Success")
                            callBack(ResultStENik.Success(auth.currentUser as T))
                        } else {
                            //Log.w(TAG_STENIK, "Create user:failure", task.exception)
                            callBack(ResultStENik.Error(task.exception.toString()))
                        }
                    }.result
            } catch (e: Exception) {
                callBack(ResultStENik.Error(e.message ?: "Unknown error"))
            }
    }

    fun <T>signIn(
        auth: FirebaseAuth,
        email: String,
        password: String,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty())
            callBack(ResultStENik.Error("Email or password is empty"))
        else
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Log.d(TAG_STENIK, "signInWithEmail:success")
                            callBack(ResultStENik.Success(auth.currentUser as T))//callBack(AuthAnswer(auth.currentUser, 4))
                        } else {
                            //Log.w(TAG_STENIK, "signInWithEmail:failure", task.exception)
                            callBack(ResultStENik.Error(task.exception.toString()))//callBack(AuthAnswer(null, 5, task.exception.toString()))
                        }
                    }
            } catch (e: Exception) {
                callBack(ResultStENik.Error(e.message ?: "Unknown error"))//callBack(AuthAnswer(null, 5, e.cause.toString()))
            }

    }

    fun <T>signOut(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        try {
            auth.signOut()
            callBack(ResultStENik.Success(auth.currentUser as T))//callBack(AuthAnswer(null, 6, ""))
        } catch (e: Exception) {
            callBack(ResultStENik.Error(e.message ?: "Unknown error"))//callBack(AuthAnswer(null, 7, e.cause.toString()))
        }

    }

    fun <T>signDelete(
        auth: FirebaseAuth,
        callBack: (ResultStENik<T>) -> Unit
    ) {
        try {
            auth.currentUser?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callBack(ResultStENik.Success(auth.currentUser as T))//callBack(AuthAnswer(null, 8, ""))
                } else {
                    callBack(ResultStENik.Error(task.exception.toString()))//callBack(AuthAnswer(null, 9, task.exception.toString()))
                }
            }
        }catch (e: Exception) {
            callBack(ResultStENik.Error(e.message ?: "Unknown error"))//callBack(AuthAnswer(null, 7, e.cause.toString()))
        }
    }
}