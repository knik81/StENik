package best.mobile.entities

import com.google.firebase.auth.FirebaseUser

data class AuthAnswer(
    val user: FirebaseUser?,
    val messageId: Int = 0,
    val messageExtra: String = ""
)
