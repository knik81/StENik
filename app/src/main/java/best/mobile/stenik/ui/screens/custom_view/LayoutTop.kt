package best.mobile.stenik.ui.screens.custom_view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import best.mobile.stenik.R

@Composable
fun LayoutTop(
    label: String,
    isBack: Boolean = true,
    accountIconId: Int,
    showAccountIcon: Boolean = true,
    onClickBack: () -> Unit,
    onClickAuthentification: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isBack)
                Image(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .clickable(true) {
                            onClickBack()
                        },
                    painter = painterResource(id = R.drawable.iconback),
                    contentDescription = "Назад"
                )
            else
                Spacer(
                    Modifier
                        .padding(end = 30.dp)
                        .size(30.dp)
                )

            Text(
                style = MaterialTheme.typography.headlineMedium,
                text = label
            )
            if (showAccountIcon)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .clickable(true) {
                                onClickAuthentification()
                            },
                        painter = painterResource(id = accountIconId),
                        contentDescription = "Назад"
                    )
                }
        }

        HorizontalDividerStENik()
    }
}