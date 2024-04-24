package abcdatoz.code.withfirebase

import abcdatoz.code.withfirebase.ui.navigation.Navigation
import abcdatoz.code.withfirebase.ui.screens.HomeScreen
import abcdatoz.code.withfirebase.ui.screens.auth.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import abcdatoz.code.withfirebase.ui.theme.WithFirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics

class MainActivity : ComponentActivity() {

    private lateinit var analytics:FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        setContent {

            WithFirebaseTheme {
                Navigation(analytics,this )
            }
        }
    }
}
