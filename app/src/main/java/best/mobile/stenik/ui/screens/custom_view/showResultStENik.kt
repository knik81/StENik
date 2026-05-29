package best.mobile.stenik.ui.screens.custom_view

import android.content.Context
import best.mobile.entities.ResultStENik

fun showResultStENik(context: Context, resultStENik: ResultStENik<String>) {
    when (resultStENik) {
        is ResultStENik.Success -> toast(context, resultStENik.data)
        is ResultStENik.Error -> toast(context, resultStENik.message)
    }
}