package best.mobile.stenik.ui.screens.custom_view

import android.content.Context
import android.view.Gravity
import android.widget.Toast

fun toast(context: Context, text: String) {
    val toast = Toast.makeText(
        context,
        text,
        Toast.LENGTH_LONG,
    )
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
    toast.show()
}