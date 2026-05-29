package best.mobile.stenik

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import best.mobile.entities.Utils.NOTIFICATION_ID
import best.mobile.entities.Utils.TAG_STENIK
import best.mobile.stenik.ui.navigation.navgraph.NavigationGraphRoot
import best.mobile.stenik.ui.theme.MyAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {




    // Ленивое внедрение MyViewModel
    private val viewModel: ViewModelMainActivity by viewModel()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme { Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavigationGraphRoot(navController = rememberNavController())
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG_STENIK,"onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_STENIK,"onDestroy")
        viewModel.stopTTS()
    }

}









